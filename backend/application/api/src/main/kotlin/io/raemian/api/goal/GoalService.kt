package io.raemian.api.goal

import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.goal.controller.request.UpdateGoalRequest
import io.raemian.api.goal.controller.response.CreateGoalResponse
import io.raemian.api.goal.controller.response.GoalResponse
import io.raemian.api.sticker.StickerService
import io.raemian.api.support.RaemianLocalDate
import io.raemian.api.support.error.MaxGoalCountExceededException
import io.raemian.api.support.error.PrivateLifeMapException
import io.raemian.api.tag.TagService
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalService(
    private val stickerService: StickerService,
    private val tagService: TagService,
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val lifeMapRepository: LifeMapRepository,
) {

    @Transactional(readOnly = true)
    fun getById(id: Long, userId: Long): GoalResponse {
        val goal = goalRepository.getById(id)
        validateAnotherUserLifeMapPublic(userId, goal.lifeMap)
        return GoalResponse(goal)
    }

    @Transactional
    fun create(userId: Long, createGoalRequest: CreateGoalRequest): CreateGoalResponse {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: createFirstLifeMap(userId)

        val goal = createGoal(createGoalRequest, lifeMap)
        addNewGoal(lifeMap, goal)

        lifeMapRepository.save(lifeMap)
        return CreateGoalResponse(goal)
    }

    @Transactional
    fun update(userId: Long, goalId: Long, updateGoalRequest: UpdateGoalRequest): GoalResponse {
        val goal = goalRepository.getById(goalId)
        validateGoalIsUsers(userId, goal)

        with(updateGoalRequest) {
            updateTag(goal, tagId)
            updateSticker(goal, stickerId)

            val deadline = RaemianLocalDate.of(yearOfDeadline, monthOfDeadline)
            goal.update(title, deadline, description)
            goalRepository.save(goal)
        }
        return GoalResponse(goal)
    }

    @Transactional
    fun delete(userId: Long, goalId: Long) {
        val goal = goalRepository.getById(goalId)
        validateGoalIsUsers(userId, goal)
        goalRepository.delete(goal)
    }

    private fun createFirstLifeMap(userId: Long): LifeMap {
        val user = userRepository.getById(userId)
        return LifeMap(user, true, goals = ArrayList())
    }

    private fun createGoal(createGoalRequest: CreateGoalRequest, lifeMap: LifeMap): Goal {
        with(createGoalRequest) {
            val deadline = RaemianLocalDate.of(yearOfDeadline, monthOfDeadline)
            val sticker = stickerService.getReferenceById(stickerId)
            val tag = tagService.getReferenceById(tagId)
            return Goal(lifeMap, title, deadline, sticker, tag, description!!)
        }
    }

    private fun validateAnotherUserLifeMapPublic(userId: Long, lifeMap: LifeMap) {
        if (lifeMap.user.id != userId && !lifeMap.isPublic) {
            throw PrivateLifeMapException()
        }
    }

    private fun validateGoalIsUsers(userId: Long, goal: Goal) {
        if (userId != goal.lifeMap.user.id) {
            throw SecurityException()
        }
    }

    private fun addNewGoal(lifeMap: LifeMap, goal: Goal) {
        try {
            lifeMap.addGoal(goal)
        } catch (exception: IllegalArgumentException) {
            throw MaxGoalCountExceededException()
        }
    }

    private fun updateTag(goal: Goal, tagId: Long) {
        if (goal.tag.id != tagId) {
            val newTag = tagService.getReferenceById(tagId)
            goal.updateTag(newTag)
        }
    }

    private fun updateSticker(goal: Goal, stickerId: Long) {
        if (goal.sticker.id != stickerId) {
            val newSticker = stickerService.getReferenceById(stickerId)
            goal.updateSticker(newSticker)
        }
    }
}
