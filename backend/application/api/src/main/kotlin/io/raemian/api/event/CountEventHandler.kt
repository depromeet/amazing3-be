package io.raemian.api.event

import io.raemian.storage.db.core.cheer.Cheering
import io.raemian.storage.db.core.cheer.CheeringRepository
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
) {
    @Transactional
    @EventListener
    fun addCheeringCount(cheeringEvent: CheeredEvent) {
        exclusiveRunner.call("cheering:${cheeringEvent.lifeMapId}", Duration.ofSeconds(10)) {
            val cheering = cheeringRepository.findByLifeMapId(cheeringEvent.lifeMapId)
                ?: Cheering(0, cheeringEvent.lifeMapId)

            cheeringRepository.save(cheering.addCount())
        }
    }

    @Transactional
    @EventListener
    fun addGoalCount(createGoalEvent: CreatedGoalEvent) {
        // TODO goal 테이블에서 life map 기준으로 전체 count 한 값으로 업데이트
        exclusiveRunner.call("goal:${createGoalEvent.lifeMapId}:${createGoalEvent.goalId}", Duration.ofSeconds(10)) {
            val mapCount = lifeMapCountRepository.findByLifeMapId(createGoalEvent.lifeMapId)
                ?: LifeMapCount.of(createGoalEvent.lifeMapId)

            val added = mapCount.addGoalCount()

            lifeMapCountRepository.save(added)
        }
    }

    @Transactional
    @EventListener
    fun addEmojiCount(reactEmojiEvent: ReactedEmojiEvent) {
        exclusiveRunner.call("emoji:${reactEmojiEvent.goalId}:${reactEmojiEvent.emojiId}", Duration.ofSeconds(10)) {
            val emoji = emojiRepository.getReferenceById(reactEmojiEvent.emojiId)
            val emojiCount = emojiCountRepository.findByGoalIdAndEmojiId(reactEmojiEvent.goalId, reactEmojiEvent.emojiId)
                ?: EmojiCount(0, emoji, reactEmojiEvent.goalId)

            emojiCountRepository.save(emojiCount.addCount())
        }
    }

    @Transactional
    @EventListener
    fun minusEmojiCount(removeEmojiEvent: RemovedEmojiEvent) {
        exclusiveRunner.call("emoji:${removeEmojiEvent.goalId}:${removeEmojiEvent.emojiId}", Duration.ofSeconds(10)) {
            val emojiCount = emojiCountRepository.findByGoalIdAndEmojiId(removeEmojiEvent.goalId, removeEmojiEvent.emojiId)

            if (emojiCount != null && 0 < emojiCount.count) {
                emojiCountRepository.save(emojiCount.minusCount())
            }
        }
    }
}
