package io.raemian.admin.lifemap

import io.raemian.admin.lifemap.controller.response.LifeMapResponse
import io.raemian.storage.db.core.cheer.CheeringRepository
import io.raemian.storage.db.core.lifemap.LifeMapCountRepository
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LifeMapService(
    private val lifeMapRepository: LifeMapRepository,
    private val cheeringRepository: CheeringRepository,
    private val lifeMapCountRepository: LifeMapCountRepository,
) {
    @Transactional(readOnly = true)
    fun findByUserId(userId: Long): LifeMapResponse {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $userId")

        lifeMap.goals = lifeMap.sortGoals().toMutableList()

        val viewCount = getViewCount(lifeMap.id!!)

        val cheeringCount = getCheeringCount(lifeMap.id!!)

        return LifeMapResponse(lifeMap, viewCount, cheeringCount)
    }

    @Transactional(readOnly = true)
    fun getViewCount(lifeMapId: Long): Long {
        val lifeMapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId = lifeMapId)
        return lifeMapCount?.viewCount ?: 0
    }

    @Transactional(readOnly = true)
    fun getCheeringCount(lifeMapId: Long): Long {
        val lifeMapCount = cheeringRepository.findByLifeMapId(lifeMapId = lifeMapId)
        return lifeMapCount?.count ?: 0
    }
}
