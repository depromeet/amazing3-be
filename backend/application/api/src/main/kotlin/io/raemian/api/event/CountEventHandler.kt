package io.raemian.api.event

import io.raemian.storage.db.core.cheer.Cheering
import io.raemian.storage.db.core.cheer.CheeringRepository
import io.raemian.storage.db.core.emoji.EmojiCount
import io.raemian.storage.db.core.emoji.EmojiCountRepository
import io.raemian.storage.db.core.lifemap.LifeMapCount
import io.raemian.storage.db.core.lifemap.LifeMapCountRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CountEventHandler(
    private val cheeringRepository: CheeringRepository,
    private val lifeMapCountRepository: LifeMapCountRepository,
    private val emojiCountRepository: EmojiCountRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    @EventListener
    fun addCheeringCount(cheeringEvent: CheeringEvent) {
        val cheering = cheeringRepository.findByLifeMapId(cheeringEvent.lifeMapId)
            ?: Cheering(0, cheeringEvent.lifeMapId)

        cheeringRepository.save(cheering.addCount())
    }

    @Transactional
    @EventListener
    fun addGoalCount(createGoalEvent: CreateGoalEvent) {
        // TODO goal 테이블에서 life map 기준으로 전체 count 한 값으로 업데이트
        val mapCount = lifeMapCountRepository.findByLifeMapId(createGoalEvent.lifeMapId)
            ?: LifeMapCount.of(createGoalEvent.lifeMapId)

        val added = mapCount.addGoalCount()

        lifeMapCountRepository.save(added)
        log.info("life map id: ${createGoalEvent.lifeMapId}, added goal count : ${added.goalCount}")
    }

    @Transactional
    @EventListener
    fun addEmojiCount(reactEmojiEvent: ReactEmojiEvent) {
        val emojiCount = emojiCountRepository.findByGoalIdAndEmojiId(reactEmojiEvent.goalId, reactEmojiEvent.emojiId)
            ?: EmojiCount(0, reactEmojiEvent.emojiId, reactEmojiEvent.goalId)

        emojiCountRepository.save(emojiCount.addCount())
    }

    @Transactional
    @EventListener
    fun minusEmojiCount(removeEmojiEvent: RemoveEmojiEvent) {
        val emojiCount = emojiCountRepository.findByGoalIdAndEmojiId(removeEmojiEvent.goalId, removeEmojiEvent.emojiId)
            ?: return

        emojiCountRepository.save(emojiCount.minusCount())
    }
}
