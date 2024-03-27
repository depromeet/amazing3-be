package io.raemian.api.goal.service

import io.raemian.api.comment.service.CommentService
import io.raemian.api.emoji.service.EmojiService
import io.raemian.api.goal.controller.request.TimelinePageRequest
import io.raemian.api.goal.model.GoalTimelineCountSubset
import io.raemian.api.goal.model.GoalTimelinePageResult
import io.raemian.api.lifemap.service.LifeMapService
import io.raemian.api.support.response.OffsetPaginationResult
import io.raemian.api.task.service.TaskService
import io.raemian.storage.db.core.goal.GoalJdbcQueryRepository
import io.raemian.storage.db.core.goal.GoalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalQueryService(
    private val lifeMapService: LifeMapService,
    private val goalJdbcQueryRepository: GoalJdbcQueryRepository,
    private val goalRepository: GoalRepository,
    private val emojiService: EmojiService,
    private val taskService: TaskService,
    private val commentService: CommentService,
) {
    @Transactional(readOnly = true)
    fun findAllByUsernameWithOffset(username: String, request: TimelinePageRequest): OffsetPaginationResult<GoalTimelinePageResult> {
        val lifeMap = lifeMapService.getFirstByUserName(username)
        val goals = goalJdbcQueryRepository.findAllByLifeMapWithOffset(lifeMap.lifeMapId, request.page, request.size)
        val total = goalRepository.countByLifeMapId(lifeMap.lifeMapId)

        val goalIds = goals.map { it.goalId }

        val goalCountMap = findGoalCountMap(goalIds)
        val reactedEmojiMap = emojiService.findAllByGoalIds(goalIds, lifeMap.user.id)

        goals.map {
            GoalTimelinePageResult.from(
                goal = it,
                counts = goalCountMap[it.goalId],
                reactedEmojisResult = reactedEmojiMap[it.goalId],
            )
        }

        return OffsetPaginationResult.of(
            request.page,
            request.size,
            total,
            goals.map {
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
}
