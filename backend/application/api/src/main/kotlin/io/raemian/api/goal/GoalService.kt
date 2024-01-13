package io.raemian.api.goal

import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.goal.controller.response.CreateGoalResponse
import io.raemian.api.goal.controller.response.GoalResponse
import io.raemian.api.sticker.StickerService
import io.raemian.api.support.RaemianLocalDate
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
    fun getById(id: Long): GoalResponse {
        val goal = goalRepository.getById(id)
        validateLifeMapPublic(goal.lifeMap)
        return GoalResponse(goal)
    }

    @Transactional
    fun create(userId: Long, createGoalRequest: CreateGoalRequest): CreateGoalResponse {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: createFirstLifeMap(userId)
        val goal = createGoal(createGoalRequest, lifeMap)

        lifeMap.addGoal(goal)
        lifeMapRepository.save(lifeMap)
        return CreateGoalResponse(goal)
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
        val (title, yearOfDeadline, monthOfDeadLine, stickerId, tagId, description) = createGoalRequest
        val deadline = RaemianLocalDate.of(yearOfDeadline, monthOfDeadLine)
        val sticker = stickerService.getById(stickerId)
        val tag = tagService.getById(tagId)
        return Goal(lifeMap, title, deadline, sticker, tag, description!!, emptyList())
    }

    private fun validateLifeMapPublic(lifeMap: LifeMap) {
        if (!lifeMap.isPublic) {
            throw SecurityException()
        }
    }

    private fun validateGoalIsUsers(userId: Long, goal: Goal) {
        if (userId != goal.lifeMap.user.id) {
            throw SecurityException()
        }
    }
}
