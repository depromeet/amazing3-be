package io.raemian.api.lifemap

import io.raemian.api.lifemap.dto.LifeMapDto
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LifeMapService(
    private val lifeMapRepository: LifeMapRepository,
) {

    @Transactional(readOnly = true)
    fun findAllByUserId(userId: Long): LifeMapResponse {
        val lifeMaps = lifeMapRepository.findAllByUserId(userId)
            .stream()
            .peek { sortByDeadlineAscendingAndCreatedAtDescending(it.goals) }
            .map { LifeMapDto(it) }
            .toList()

        return LifeMapResponse(lifeMaps)
    }

    @Transactional(readOnly = true)
    fun findAllByUserName(userName: String): LifeMapResponse {
        val lifeMaps = lifeMapRepository.findAllByUserUserName(userName)
            .ifEmpty { throw NoSuchElementException("존재하지 않는 유저입니다. $userName") }

        return createLifeMapResponse(lifeMaps)
    }

    @Transactional
    fun updatePublication(userId: Long, updatePublicationRequest: UpdatePublicationRequest) {
        val lifeMap = lifeMapRepository.findAllByUserId(userId)
            .first()
        lifeMap.updatePublication(updatePublicationRequest.isPublic)
    }

    private fun createLifeMapResponse(lifeMaps: List<LifeMap>): LifeMapResponse {
        val sortedLifeMaps = lifeMaps.stream()
            .filter { it.isPublic }
            .peek { sortByDeadlineAscendingAndCreatedAtDescending(it.goals) }
            .map { LifeMapDto(it) }
            .toList()
            .ifEmpty { throw SecurityException("Public 상태인 지도가 없습니다.") }

        return LifeMapResponse(sortedLifeMaps)
    }

    private fun sortByDeadlineAscendingAndCreatedAtDescending(goals: List<Goal>): List<Goal> =
        goals.sortedWith(
            compareBy<Goal> { it.deadline }
                .thenByDescending { it.createdAt },
        )
}
