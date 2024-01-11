package io.raemian.api.lifemap

import io.raemian.api.lifemap.controller.LifeMapResponse
import io.raemian.api.lifemap.controller.UpdatePublicRequest
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LifeMapService(
    private val lifeMapRepository: LifeMapRepository,
) {

    @Transactional(readOnly = true)
    fun findFirstByUserId(userId: Long): LifeMapResponse {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $userId")

        sortByDeadlineAscendingAndCreatedAtDescending(lifeMap.goals)
        return LifeMapResponse(lifeMap)
    }

    @Transactional(readOnly = true)
    fun findFirstByUserName(username: String): LifeMapResponse {
        val lifeMap = lifeMapRepository.findFirstByUserUsername(username)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $username")

        validateLifeMapPublic(lifeMap)
        sortByDeadlineAscendingAndCreatedAtDescending(lifeMap.goals)
        return LifeMapResponse(lifeMap)
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
        takeUnless { lifeMap.isPublic }
            .apply { throw SecurityException() }
}
