package io.raemian.api.emoji

import io.raemian.api.emoji.controller.response.EmojiResponse
import io.raemian.api.emoji.controller.response.ReactedEmojisResponse
import io.raemian.api.event.ReactedEmojiEvent
import io.raemian.api.event.RemovedEmojiEvent
import io.raemian.storage.db.core.emoji.EmojiRepository
import io.raemian.storage.db.core.emoji.ReactedEmoji
import io.raemian.storage.db.core.emoji.ReactedEmojiRepository
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.dao.DataIntegrityViolationException
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
    fun findAll(): List<EmojiResponse> =
        emojiRepository.findAll()
            .map(EmojiResponse::from)

    @Transactional(readOnly = true)
    fun findAllReactedEmojisByGoalId(goalId: Long, currentUserId: Long): ReactedEmojisResponse {
        val goal = goalRepository.getReferenceById(goalId)
        val reactedEmojis = reactedEmojiRepository.findAllByGoal(goal)
        return ReactedEmojisResponse.of(reactedEmojis, currentUserId)
    }

    @Transactional
    fun react(emojiId: Long, goalId: Long, emojiReactUserId: Long) {
        val emoji = emojiRepository.getReferenceById(emojiId)
        val goal = goalRepository.getReferenceById(goalId)
        val emojiReactUser = userRepository.getReferenceById(emojiReactUserId)
        val reactedEmoji = ReactedEmoji(goal, emoji, emojiReactUser)

        ignoreDuplicatedReactedEmojiException {
            reactedEmojiRepository.save(reactedEmoji)
        }

        applicationEventPublisher.publishEvent(ReactedEmojiEvent(goalId, emojiId))
    }

    @Transactional
    fun remove(emojiId: Long, goalId: Long, emojiReactUserId: Long) {
        val emoji = emojiRepository.getReferenceById(emojiId)
        val goal = goalRepository.getReferenceById(goalId)
        val emojiReactUser = userRepository.getReferenceById(emojiReactUserId)

        reactedEmojiRepository
            .deleteByEmojiAndGoalAndReactUser(emoji, goal, emojiReactUser)

        applicationEventPublisher.publishEvent(RemovedEmojiEvent(goalId, emojiId))
    }
}

inline fun ignoreDuplicatedReactedEmojiException(action: () -> Any) {
    try {
        action()
    } catch (exception: DataIntegrityViolationException) {}
}
