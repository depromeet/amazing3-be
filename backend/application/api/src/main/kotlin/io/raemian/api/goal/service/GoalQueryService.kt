package io.raemian.api.goal.service

import io.raemian.api.comment.service.CommentService
import io.raemian.api.emoji.service.EmojiService
import io.raemian.api.goal.controller.request.TimelinePageRequest
import io.raemian.api.goal.model.GoalTimelineCountSubset
import io.raemian.api.goal.model.GoalTimelinePageResult
import io.raemian.api.lifemap.service.LifeMapService
import io.raemian.api.support.constant.LocalDateTimeConstant
import io.raemian.api.support.response.PaginationResult
import io.raemian.api.task.service.TaskService
import io.raemian.storage.db.core.common.pagination.CursorPaginationResult
import io.raemian.storage.db.core.common.pagination.CursorPaginationTemplate
import io.raemian.storage.db.core.goal.GoalJdbcQueryRepository
import io.raemian.storage.db.core.goal.model.GoalQueryResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class GoalQueryService(
    private val lifeMapService: LifeMapService,
    private val goalJdbcQueryRepository: GoalJdbcQueryRepository,
    private val emojiService: EmojiService,
    private val taskService: TaskService,
    private val commentService: CommentService,
) {
    @Transactional(readOnly = true)
    fun findAllByUsernameWithCursor(username: String, request: TimelinePageRequest): PaginationResult<GoalTimelinePageResult> {
        val lifeMap = lifeMapService.getFirstByUserName(username)
        val goals = findAllByLifeMapIdWithCursor(lifeMap.lifeMapId, request)

        val goalIds = goals.contents.map { it.goalId }

        val goalCountMap = findGoalCountMap(goalIds)
        val reactedEmojiMap = emojiService.findAllByGoalIds(goalIds, lifeMap.user.id)

        return PaginationResult.from(
            lifeMap.goals.size,
            goals.transform {
                GoalTimelinePageResult.from(
                    goal = it,
                    counts = goalCountMap[it.goalId],
                    reactedEmojisResult = reactedEmojiMap[it.goalId],
                )
            },
        )
    }

    private fun findGoalCountMap(goalIds: List<Long>): Map<Long, GoalTimelineCountSubset> {
        val commentCountMap = commentService.findGoalCommentCounts(goalIds).associate { it.goalId to it.commentCount }
        val taskCountMap = taskService.findGoalTaskCounts(goalIds).associate { it.goalId to it.taskCount }

        return goalIds.associateWith {
            GoalTimelineCountSubset.of(
                commentCountMap[it] ?: 0,
                taskCountMap[it] ?: 0,
            )
        }
    }

    private fun findAllByLifeMapIdWithCursor(lifeMapId: Long, request: TimelinePageRequest): CursorPaginationResult<GoalQueryResult> {
        return CursorPaginationTemplate.execute(lifeMapId, request.cursor ?: LocalDateTimeConstant.MAX, request.size) {
                id, cursor, size ->
            goalJdbcQueryRepository.findAllByLifeMapWithCursor(id, cursor, size)
        }
    }
}
