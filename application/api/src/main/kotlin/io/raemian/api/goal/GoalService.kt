package io.raemian.api.goal

import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.goal.controller.response.CreateGoalResponse
import io.raemian.api.sticker.StickerService
import io.raemian.api.support.RaemianLocalDate
import io.raemian.api.tag.TagService
import io.raemian.api.user.UserService
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalService(
    private val userService: UserService,
    private val stickerService: StickerService,
    private val tagService: TagService,
    private val goalRepository: GoalRepository,
) {
    @Transactional
    fun create(userId: Long, createGoalRequest: CreateGoalRequest): CreateGoalResponse {
        val (title, yearOfDeadline, monthOfDeadLine, stickerId, tagId, description) = createGoalRequest

        val deadline = RaemianLocalDate.of(yearOfDeadline, monthOfDeadLine)
        val sticker = stickerService.getById(stickerId)
        val tag = tagService.getById(tagId)
        val user = userService.getById(userId)

        val goal = Goal(user, title, deadline, sticker, tag, description!!, emptyList())
        goalRepository.save(goal)
        return CreateGoalResponse(goal)
    }

    @Transactional
    fun delete(userId: Long, goalId: Long) {
        val goal = goalRepository.getById(goalId)
        validateGoalIsUsers(userId, goal)
        goalRepository.delete(goal)
    }

    private fun validateGoalIsUsers(userId: Long, goal: Goal) {
        if (userId != goal.user.id) {
            throw SecurityException()
        }
    }
}
