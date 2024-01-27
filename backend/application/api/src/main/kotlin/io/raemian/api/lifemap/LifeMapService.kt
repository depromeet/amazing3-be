package io.raemian.api.lifemap

import io.raemian.api.lifemap.domain.LifeMapCountDTO
import io.raemian.api.lifemap.domain.LifeMapDTO
import io.raemian.api.lifemap.domain.UpdatePublicRequest
import io.raemian.api.support.error.PrivateLifeMapException
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.lifemap.LifeMapCount
import io.raemian.storage.db.core.lifemap.LifeMapCountRepository
import io.raemian.storage.db.core.lifemap.LifeMapHistory
import io.raemian.storage.db.core.lifemap.LifeMapHistoryRepository
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LifeMapService(
    private val lifeMapRepository: LifeMapRepository,
    private val userRepository: UserRepository,
    private val lifeMapCountRepository: LifeMapCountRepository,
    private val lifeMapHistoryRepository: LifeMapHistoryRepository,
) {

    @Transactional(readOnly = true)
    fun findFirstByUserId(userId: Long): LifeMapDTO {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $userId")

        // TODO edit immutable
        lifeMap.goals = lifeMap.sortGoals().toMutableList()

        return LifeMapDTO(lifeMap)
    }

    @Transactional(readOnly = true)
    fun findFirstByUserName(username: String): LifeMapDTO {
        val lifeMap = lifeMapRepository.findFirstByUserUsername(username)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $username")

        validateLifeMapPublic(lifeMap)

        // TODO edit immutable
        lifeMap.goals = lifeMap.sortGoals().toMutableList()

        val user = userRepository.getById(lifeMap.user.id!!)
        return LifeMapDTO(lifeMap, user)
    }

    @Transactional
    fun updatePublic(userId: Long, updatePublicRequest: UpdatePublicRequest) {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $userId")
        lifeMap.updatePublic(updatePublicRequest.isPublic)
    }

    @Transactional
    fun getLifeMapCount(lifeMapId: Long): LifeMapCountDTO {
        val lifeMapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId = lifeMapId)
            ?: lifeMapCountRepository.save(LifeMapCount.of(lifeMapId))
        return LifeMapCountDTO(lifeMapCount)
    }

    @Transactional(readOnly = true)
    fun getViewCount(lifeMapId: Long): Long {
        val lifeMapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId = lifeMapId)
        return lifeMapCount?.viewCount ?: 0
    }

    @Transactional(readOnly = true)
    fun getHistoryCount(lifeMapId: Long): Long {
        val lifeMapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId = lifeMapId)
        return lifeMapCount?.historyCount ?: 0
    }

    @Transactional
    fun addViewCount(lifeMapId: Long): Long {
        val lifeMapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId)
            ?: LifeMapCount.of(lifeMapId)
        val added = lifeMapCount.addViewCount()
        val saved = lifeMapCountRepository.save(added)
        return saved.viewCount
    }

    @Async
    fun addHistoryCount(lifeMapId: Long) {
        val lifeMapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId)
            ?: LifeMapCount.of(lifeMapId)
        val added = lifeMapCount.addHistoryCount()
        val saved = lifeMapCountRepository.save(added)
    }

    @Async
    fun upsertLifeMapHistory(userId: Long, lifeMapId: Long) {
        val history = lifeMapHistoryRepository.findByLifeMapIdAndUserId(lifeMapId = lifeMapId, userId = userId)

        if (history == null) {
            addHistoryCount(lifeMapId)
            lifeMapHistoryRepository.save(LifeMapHistory.of(lifeMapId = lifeMapId, userId = userId))
        }
    }

    @Transactional
    fun addCount(lifeMapId: Long): Long {
        val lifeMapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId)
            ?: LifeMapCount.of(lifeMapId)
        val added = lifeMapCount.addCount()
        val saved = lifeMapCountRepository.save(added)
        return saved.historyCount
    }


    private fun validateLifeMapPublic(lifeMap: LifeMap) =
        takeIf { lifeMap.isPublic } ?: throw PrivateLifeMapException()
}
