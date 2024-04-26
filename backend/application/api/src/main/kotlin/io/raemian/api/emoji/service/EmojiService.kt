package io.raemian.api.emoji.service

import io.raemian.api.emoji.model.EmojiResult
import io.raemian.api.emoji.model.ReactedEmojisResult
import io.raemian.api.event.model.ReactedEmojiEvent
import io.raemian.api.event.model.RemovedEmojiEvent
import io.raemian.storage.db.core.emoji.EmojiRepository
import io.raemian.storage.db.core.emoji.ReactedEmoji
import io.raemian.storage.db.core.emoji.ReactedEmojiRepository
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmojiService(
    private val emojiRepository: EmojiRepository,
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val reactedEmojiRepository: ReactedEmojiRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @Transactional(readOnly = true)
    fun findAll(): List<EmojiResult> =
        emojiRepository.findAll()
            .map(EmojiResult::from)

    @Transactional(readOnly = true)
    fun findAllReactedEmojisByGoalId(goalId: Long, currentUserId: Long): ReactedEmojisResult {
        val goal = goalRepository.getReferenceById(goalId)
        val reactedEmojis = reactedEmojiRepository.findAllByGoal(goal)
        return ReactedEmojisResult.of(reactedEmojis, currentUserId)
    }

    @Transactional
    fun react(emojiId: Long, goalId: Long, emojiReactUserId: Long) {
        val emoji = emojiRepository.getReferenceById(emojiId)
        val goal = goalRepository.getReferenceById(goalId)
        val emojiReactUser = userRepository.getReferenceById(emojiReactUserId)

        if (isReactedEmojiNotExist(
                reactUserId = emojiReactUserId,
                goalId = goalId,
                emojiId = emojiId,
            )
        ) {
            val reactedEmoji = ReactedEmoji(goal, emoji, emojiReactUser)
            reactedEmojiRepository.save(reactedEmoji)
            applicationEventPublisher.publishEvent(ReactedEmojiEvent(goalId, emojiId))
        }
    }

    private fun isReactedEmojiNotExist(reactUserId: Long, goalId: Long, emojiId: Long) =
        reactedEmojiRepository.existsByReactUserIdAndGoalIdAndEmojiId(
            reactUserId = reactUserId,
            goalId = goalId,
            emojiId = emojiId,
        ).not()

    @Transactional
    fun remove(emojiId: Long, goalId: Long, emojiReactUserId: Long) {
        val emoji = emojiRepository.getReferenceById(emojiId)
        val goal = goalRepository.getReferenceById(goalId)
        val emojiReactUser = userRepository.getReferenceById(emojiReactUserId)

        reactedEmojiRepository
            .deleteByEmojiAndGoalAndReactUser(emoji, goal, emojiReactUser)

        applicationEventPublisher.publishEvent(RemovedEmojiEvent(goalId, emojiId))
    }

    @Transactional(readOnly = true)
    fun findAllByGoalIds(ids: List<Long>, userId: Long?): Map<Long, ReactedEmojisResult> {
        return reactedEmojiRepository.findAllByGoalIdIn(ids)
            .groupBy { it.goal.id!! }
            .mapValues { ReactedEmojisResult.of(it.value, userId ?: -1) }
    }
}
