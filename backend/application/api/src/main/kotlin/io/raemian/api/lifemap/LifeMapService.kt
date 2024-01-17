package io.raemian.api.lifemap

import io.raemian.api.lifemap.domain.LifeMapResponse
import io.raemian.api.lifemap.domain.UpdatePublicRequest
import io.raemian.api.support.error.PrivateLifeMapException
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LifeMapService(
    private val lifeMapRepository: LifeMapRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    fun findFirstByUserId(userId: Long): LifeMapResponse {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $userId")

        // TODO edit immutable
        lifeMap.goals = lifeMap.sortGoals().toMutableList()

        return LifeMapResponse(lifeMap)
    }

    @Transactional(readOnly = true)
    fun findFirstByUserName(username: String): LifeMapResponse {
        val lifeMap = lifeMapRepository.findFirstByUserUsername(username)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $username")

        validateLifeMapPublic(lifeMap)

        // TODO edit immutable
        lifeMap.goals = lifeMap.sortGoals().toMutableList()

        val user = userRepository.getById(lifeMap.user.id!!)
        return LifeMapResponse(lifeMap, user)
    }

    @Transactional
    fun updatePublic(userId: Long, updatePublicRequest: UpdatePublicRequest) {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $userId")
        lifeMap.updatePublic(updatePublicRequest.isPublic)
    }

    private fun sortByDeadlineAscendingAndCreatedAtDescending(goals: List<Goal>): List<Goal> =
        goals.sortedWith(
            compareBy<Goal> { it.deadline }
                .thenByDescending { it.createdAt },
        )

    private fun validateLifeMapPublic(lifeMap: LifeMap) =
        takeIf { lifeMap.isPublic } ?: throw PrivateLifeMapException()
}
