package io.raemian.api.lifemap

import io.raemian.api.lifemap.domain.LifeMapDTO
import io.raemian.api.lifemap.domain.UpdatePublicRequest
import io.raemian.api.support.error.PrivateLifeMapException
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.lifemap.LifeMapCount
import io.raemian.storage.db.core.lifemap.LifeMapCountRepository
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LifeMapService(
    private val lifeMapRepository: LifeMapRepository,
    private val userRepository: UserRepository,
    private val lifeMapCountRepository: LifeMapCountRepository,
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

    @Transactional(readOnly = true)
    fun getCount(lifeMapId: Long): Long {
        val lifeMapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId = lifeMapId)
        return lifeMapCount?.count ?: 0
    }

    @Transactional
    fun addCount(lifeMapId: Long): Long {
        val lifeMapCount = lifeMapCountRepository.findByLifeMapId(lifeMapId)
            ?: LifeMapCount(
                lifeMapId = lifeMapId,
                count = 0,
            )
        val added = lifeMapCount.addCount()
        val saved = lifeMapCountRepository.save(added)
        return saved.count
    }

    private fun validateLifeMapPublic(lifeMap: LifeMap) =
        takeIf { lifeMap.isPublic } ?: throw PrivateLifeMapException()
}
