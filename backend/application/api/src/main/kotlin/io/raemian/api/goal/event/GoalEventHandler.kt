package io.raemian.api.goal.event

import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.lifemap.LifeMapCount
import io.raemian.storage.db.core.lifemap.LifeMapCountRepository
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class GoalEventHandler(
    private val lifeMapCountRepository: LifeMapCountRepository,
    private val goalRepository: GoalRepository,
    private val lifeMapRepository: LifeMapRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

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