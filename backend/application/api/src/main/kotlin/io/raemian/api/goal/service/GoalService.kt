package io.raemian.api.goal.service

import io.raemian.api.emoji.service.EmojiService
import io.raemian.api.event.model.CreatedGoalEvent
import io.raemian.api.event.model.DeletedGoalEvent
import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.goal.controller.request.UpdateGoalRequest
import io.raemian.api.goal.model.CreateGoalResult
import io.raemian.api.goal.model.GoalExploreResult
import io.raemian.api.goal.model.GoalResult
import io.raemian.api.sticker.service.StickerService
import io.raemian.api.support.exception.MaxGoalCountExceededException
import io.raemian.api.support.exception.PrivateLifeMapException
import io.raemian.api.support.utils.DeadlineCreator
import io.raemian.api.tag.service.TagService
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class GoalService(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val lifeMapRepository: LifeMapRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val emojiService: EmojiService,
    private val stickerService: StickerService,
    private val tagService: TagService,
) {

    @Transactional(readOnly = true)
    fun getById(id: Long, userId: Long): GoalResult {
        val goal = goalRepository.getById(id)
        val isMyGoal = goal.lifeMap.user.id == userId
        validateAnotherUserLifeMapPublic(isMyGoal, goal.lifeMap)
        return GoalResult(goal, isMyGoal)
    }

    @Transactional
    fun create(userId: Long, createGoalRequest: CreateGoalRequest): CreateGoalResult {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: createFirstLifeMap(userId)

        val goal = createGoal(createGoalRequest, lifeMap)
        addNewGoal(lifeMap, goal)

        lifeMapRepository.save(lifeMap)

        // goal 생성시 count event 발행
        applicationEventPublisher.publishEvent(
            CreatedGoalEvent(lifeMap.id!!),
        )

        return CreateGoalResult(goal)
    }

    @Transactional
    fun update(userId: Long, goalId: Long, updateGoalRequest: UpdateGoalRequest): GoalResult {
        val goal = goalRepository.getById(goalId)
        validateGoalIsUsers(userId, goal)

        with(updateGoalRequest) {
            val updateTagGoal = goal.takeIf { it.tag.id == tagId }
                ?: updateTag(goal, tagId)

            val updatedStickerGoal = updateTagGoal.takeIf { it.sticker.id == stickerId }
                ?: updateSticker(updateTagGoal, stickerId)

            val deadline = DeadlineCreator.create(yearOfDeadline, monthOfDeadline)
            val updatedGoal = updatedStickerGoal.update(title, deadline, description)
            goalRepository.save(updatedGoal)
            return GoalResult(updatedGoal, true)
        }
    }

    @Transactional
    fun delete(userId: Long, goalId: Long) {
        val goal = goalRepository.getById(goalId)
        validateGoalIsUsers(userId, goal)
        goalRepository.delete(goal)

        applicationEventPublisher.publishEvent(
            DeletedGoalEvent(goal.lifeMap.id!!),
        )
    }

    @Transactional(readOnly = true)
    fun explore(goalId: Long, userId: Long?): List<GoalExploreResult> {
        val explore = goalRepository.explore(goalId)
        val goalIds = explore.map { it.goalId }

        val reactedEmojiMap = emojiService.findAllByGoalIds(goalIds, userId)

        return explore
            .map { GoalExploreResult.from(it, reactedEmojiMap[it.goalId]) }
    }

    private fun createFirstLifeMap(userId: Long): LifeMap {
        val user = userRepository.getReferenceById(userId)
        return LifeMap(
            user = user,
            isPublic = true,
            goals = ArrayList(),
        )
    }

    private fun createGoal(createGoalRequest: CreateGoalRequest, lifeMap: LifeMap): Goal {
        with(createGoalRequest) {
            val deadline = DeadlineCreator.create(yearOfDeadline, monthOfDeadline)
            val sticker = stickerService.getReferenceById(stickerId)
            val tag = tagService.getReferenceById(tagId)
            return Goal(
                lifeMap = lifeMap,
                title = title,
                deadline = deadline,
                sticker = sticker,
                tag = tag,
                description = description!!,
                lastCommentReadAt = LocalDateTime.now(),
            )
        }
    }

    private fun validateAnotherUserLifeMapPublic(isMyGoal: Boolean, lifeMap: LifeMap) {
        if (!isMyGoal && !lifeMap.isPublic) {
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

    private fun updateTag(goal: Goal, tagId: Long): Goal {
        val newTag = tagService.getReferenceById(tagId)
        return goal.updateTag(newTag)
    }

    private fun updateSticker(goal: Goal, stickerId: Long): Goal {
        val newSticker = stickerService.getReferenceById(stickerId)
        return goal.updateSticker(newSticker)
    }
}
