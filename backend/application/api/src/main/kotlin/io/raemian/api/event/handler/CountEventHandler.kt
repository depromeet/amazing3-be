package io.raemian.api.event.handler

import io.raemian.api.event.model.CheeredEvent
import io.raemian.api.event.model.CreatedCommentEvent
import io.raemian.api.event.model.CreatedGoalEvent
import io.raemian.api.event.model.DeletedCommentEvent
import io.raemian.api.event.model.DeletedGoalEvent
import io.raemian.api.event.model.ReactedEmojiEvent
import io.raemian.api.event.model.RemovedEmojiEvent
import io.raemian.api.support.lock.ExclusiveRunner
import io.raemian.storage.db.core.cheer.Cheering
import io.raemian.storage.db.core.cheer.CheeringRepository
import io.raemian.storage.db.core.comment.CommentCount
import io.raemian.storage.db.core.comment.CommentCountRepository
import io.raemian.storage.db.core.emoji.EmojiCount
import io.raemian.storage.db.core.emoji.EmojiCountRepository
import io.raemian.storage.db.core.emoji.EmojiRepository
import io.raemian.storage.db.core.lifemap.LifeMapCount
import io.raemian.storage.db.core.lifemap.LifeMapCountRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
class CountEventHandler(
    private val cheeringRepository: CheeringRepository,
    private val lifeMapCountRepository: LifeMapCountRepository,
    private val emojiCountRepository: EmojiCountRepository,
    private val exclusiveRunner: ExclusiveRunner,
    private val emojiRepository: EmojiRepository,
    private val commentCountRepository: CommentCountRepository,
) {
    @Transactional
    @EventListener
    fun addCheeringCount(event: CheeredEvent) {
        exclusiveRunner.call("cheering:${event.lifeMapId}", Duration.ofSeconds(10)) {
            val cheering = cheeringRepository.findByLifeMapId(event.lifeMapId)
                ?: Cheering(0, event.lifeMapId)

            cheeringRepository.save(cheering.addCount())
        }
    }

    @Transactional
    @EventListener
    fun addGoalCount(event: CreatedGoalEvent) {
        // TODO goal 테이블에서 life map 기준으로 전체 count 한 값으로 업데이트
        exclusiveRunner.call("goal:${event.lifeMapId}", Duration.ofSeconds(10)) {
            val mapCount = lifeMapCountRepository.findByLifeMapId(event.lifeMapId)
                ?: LifeMapCount.of(event.lifeMapId)

            val added = mapCount.addGoalCount()

            lifeMapCountRepository.save(added)
        }
    }

    @Transactional
    @EventListener
    fun minusGoalCount(event: DeletedGoalEvent) {
        exclusiveRunner.call("goal:${event.lifeMapId}", Duration.ofSeconds(10)) {
            val mapCount = lifeMapCountRepository.findByLifeMapId(event.lifeMapId)
                ?: LifeMapCount.of(event.lifeMapId)

            lifeMapCountRepository.save(mapCount.minusGoalCount())
        }
    }

    @Transactional
    @EventListener
    fun addEmojiCount(event: ReactedEmojiEvent) {
        exclusiveRunner.call("emoji:${event.goalId}:${event.emojiId}", Duration.ofSeconds(10)) {
            val emoji = emojiRepository.getReferenceById(event.emojiId)
            val emojiCount = emojiCountRepository.findByGoalIdAndEmojiId(event.goalId, event.emojiId)
                ?: EmojiCount(0, emoji, event.goalId)

            emojiCountRepository.save(emojiCount.addCount())
        }
    }

    @Transactional
    @EventListener
    fun minusEmojiCount(event: RemovedEmojiEvent) {
        exclusiveRunner.call("emoji:${event.goalId}:${event.emojiId}", Duration.ofSeconds(10)) {
            val emojiCount = emojiCountRepository.findByGoalIdAndEmojiId(event.goalId, event.emojiId)
                ?: EmojiCount(count = 0, goalId = event.goalId, emoji = emojiRepository.getReferenceById(event.emojiId))

            emojiCountRepository.save(emojiCount.minusCount())
        }
    }

    @Transactional
    @EventListener
    fun addCommentCount(event: CreatedCommentEvent) {
        exclusiveRunner.call("comment:${event.goalId}", Duration.ofSeconds(10)) {
            val commentCount = commentCountRepository.findByGoalId(event.goalId)
                ?: CommentCount(0, event.goalId)

            commentCountRepository.save(commentCount.addCount())
        }
    }

    @Transactional
    @EventListener
    fun minusCommentCount(event: DeletedCommentEvent) {
        val commentCount = commentCountRepository.findByGoalId(event.goalId)
            ?: CommentCount(count = 0, goalId = event.goalId)

        commentCountRepository.save(commentCount.minusCount())
    }
}
