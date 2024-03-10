package io.raemian.api.event

import io.raemian.storage.db.core.cheer.Cheering
import io.raemian.storage.db.core.cheer.CheeringRepository
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.lifemap.LifeMapCount
import io.raemian.storage.db.core.lifemap.LifeMapCountRepository
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CountEventHandler(
    private val cheeringRepository: CheeringRepository,
    private val lifeMapCountRepository: LifeMapCountRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    @EventListener
    fun addCheeringCount(cheeringEvent: CheeringEvent) {
        val cheering = cheeringRepository.findByLifeMapIdForUpdate(cheeringEvent.lifeMapId)
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
}
