package io.raemian.api.goal.service

import io.raemian.api.comment.service.CommentService
import io.raemian.api.emoji.service.EmojiService
import io.raemian.api.goal.controller.request.TimelinePageRequest
import io.raemian.api.goal.model.GoalTimelineCountSubset
import io.raemian.api.goal.model.GoalTimelinePageResult
import io.raemian.api.lifemap.service.LifeMapService
import io.raemian.api.support.response.PaginationResult
import io.raemian.api.task.service.TaskService
import io.raemian.storage.db.core.common.pagination.CursorPaginationResult
import io.raemian.storage.db.core.common.pagination.CursorPaginationTemplate
import io.raemian.storage.db.core.goal.GoalJdbcQueryRepository
import io.raemian.storage.db.core.goal.model.GoalQueryResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalQueryService(
    private val lifeMapService: LifeMapService,
    private val goalJdbcQueryRepository: GoalJdbcQueryRepository,
    private val emojiService: EmojiService,
    private val taskService: TaskService,
    private val commentService: CommentService,
) {
    @Transactional(readOnly = true)
    fun getTimeline(userId: Long, request: TimelinePageRequest): PaginationResult<GoalTimelinePageResult> {
        val lifeMap = lifeMapService.findFirstByUserId(userId)

        val goals = findAllGoalWithCursor(lifeMap.lifeMapId, request)

        val goalIds = goals.contents.map { it.goalId }

        val goalTimelineCountMap = findGoalTimelineCountMap(goalIds)
        val reactedEmojiMap = emojiService.findAllByGoalIds(goalIds, userId)

        return PaginationResult.from(
            lifeMap.goals.size.toLong(),
            goals.transform() {
                    goal ->
                GoalTimelinePageResult.from(
                    goal,
                    goalTimelineCountMap[goal.goalId],
                    reactedEmojiMap[goal.goalId],
                )
            },
        )
    }

    private fun findGoalTimelineCountMap(goalIds: List<Long>): Map<Long, GoalTimelineCountSubset> {
        val commentCountMap = commentService.findGoalCommentCountMap(goalIds)
        val taskCountMap = taskService.findGoalTaskCountMap(goalIds)

        return goalIds.associate {
            it to GoalTimelineCountSubset.of(
                commentCountMap.get(1L) ?: 0,
                taskCountMap.get(1L) ?: 0,
            )
        }
    }

    private fun findAllGoalWithCursor(lifeMapId: Long, request: TimelinePageRequest): CursorPaginationResult<GoalQueryResult> {
        return CursorPaginationTemplate.execute(lifeMapId, request.cursor ?: Long.MAX_VALUE, request.size) {
                id, cursor, size ->
            goalJdbcQueryRepository.findAllByLifeMapWithCursor(id, cursor, size)
        }
    }
}
