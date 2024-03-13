package io.raemian.api.goal

import io.raemian.api.event.CreatedGoalEvent
import io.raemian.api.goal.controller.request.CreateGoalRequest
import io.raemian.api.goal.controller.request.UpdateGoalRequest
import io.raemian.api.goal.controller.response.CreateGoalResponse
import io.raemian.api.goal.controller.response.GoalResponse
import io.raemian.api.goal.domain.GoalExploreDTO
import io.raemian.api.sticker.StickerService
import io.raemian.api.support.RaemianLocalDate
import io.raemian.api.support.error.MaxGoalCountExceededException
import io.raemian.api.support.error.PrivateLifeMapException
import io.raemian.api.tag.TagService
import io.raemian.storage.db.core.emoji.EmojiCountRepository
import io.raemian.storage.db.core.emoji.ReactedEmojiRepository
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
    private val stickerService: StickerService,
    private val tagService: TagService,
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val lifeMapRepository: LifeMapRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val emojiCountRepository: EmojiCountRepository,
    private val reactedEmojiRepository: ReactedEmojiRepository
) {

    @Transactional(readOnly = true)
    fun getById(id: Long, userId: Long): GoalResponse {
        val goal = goalRepository.getById(id)
        val isMyGoal = goal.lifeMap.user.id == userId
        validateAnotherUserLifeMapPublic(isMyGoal, goal.lifeMap)
        return GoalResponse(goal, isMyGoal)
    }

    @Transactional
    fun create(userId: Long, createGoalRequest: CreateGoalRequest): CreateGoalResponse {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: createFirstLifeMap(userId)

        val goal = createGoal(createGoalRequest, lifeMap)
        addNewGoal(lifeMap, goal)

        lifeMapRepository.save(lifeMap)

        // goal 생성시 count event 발행
        applicationEventPublisher.publishEvent(
            CreatedGoalEvent(goalId = goal.id!!, lifeMapId = lifeMap.id!!),
        )

        return CreateGoalResponse(goal)
    }

    @Transactional
    fun update(userId: Long, goalId: Long, updateGoalRequest: UpdateGoalRequest): GoalResponse {
        val goal = goalRepository.getById(goalId)
        validateGoalIsUsers(userId, goal)

        with(updateGoalRequest) {
            val updateTagGoal = goal.takeIf { it.tag.id == tagId }
                ?: updateTag(goal, tagId)

            val updatedStickerGoal = updateTagGoal.takeIf { it.sticker.id == stickerId }
                ?: updateSticker(updateTagGoal, stickerId)

            val deadline = RaemianLocalDate.of(yearOfDeadline, monthOfDeadline)
            val updatedGoal = updatedStickerGoal.update(title, deadline, description)
            goalRepository.save(updatedGoal)
            return GoalResponse(updatedGoal, true)
        }
    }

    @Transactional
    fun delete(userId: Long, goalId: Long) {
        val goal = goalRepository.getById(goalId)
        validateGoalIsUsers(userId, goal)
        goalRepository.delete(goal)
    }

    @Transactional(readOnly = true)
    fun explore(goalId: Long): List<GoalExploreDTO> {
        val explore = goalRepository.explore(goalId)
        val goalIds = explore.map { it.goalId }

        val emojiGroup = emojiCountRepository.findAllByGoalIdIn(goalIds).groupBy { it.goalId }

        return explore
            .map { GoalExploreDTO.from(it, emojiGroup[it.goalId] ?: listOf()) }
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
            val deadline = RaemianLocalDate.of(yearOfDeadline, monthOfDeadline)
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
