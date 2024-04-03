package io.raemian.api.lifemap.service

import io.raemian.storage.db.core.lifemap.LifeMapCountRepository
import org.springframework.stereotype.Service

@Service
class LifeMapCountQueryService(
    private val lifeMapCountRepository: LifeMapCountRepository,
) {
    fun findGoalCount(lifeMapId: Long): Long {
        val mapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId) ?: return 0
        return mapCount.goalCount
    }
}
