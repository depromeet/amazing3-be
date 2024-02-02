package io.raemian.api.cheer.event

import io.raemian.storage.db.core.cheer.Cheering
import io.raemian.storage.db.core.cheer.CheeringRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CheeringEventHandler(
    private val cheeringRepository: CheeringRepository,
) {

    @Transactional
    @EventListener
    fun addCheeringCount(cheeringEvent: CheeringEvent) {
        val cheering = cheeringRepository.findByLifeMapIdForUpdate(cheeringEvent.lifeMapId)
            ?: Cheering(0, cheeringEvent.lifeMapId)

        cheeringRepository.save(cheering.addCount())
    }
}
