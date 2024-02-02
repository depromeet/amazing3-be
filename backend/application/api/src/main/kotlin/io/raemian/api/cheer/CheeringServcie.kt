package io.raemian.api.cheer

import io.raemian.api.cheer.controller.request.CheeringRequest
import io.raemian.api.cheer.controller.request.CheeringSquadPagingRequest
import io.raemian.api.cheer.controller.response.CheererResponse
import io.raemian.api.cheer.controller.response.CheeringCountResponse
import io.raemian.api.cheer.event.CheeringEvent
import io.raemian.api.support.error.CoreApiException
import io.raemian.api.support.error.ErrorInfo
import io.raemian.api.support.response.PageResult
import io.raemian.storage.db.core.cheer.Cheerer
import io.raemian.storage.db.core.cheer.CheererRepository
import io.raemian.storage.db.core.cheer.Cheering
import io.raemian.storage.db.core.cheer.CheeringRepository
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CheeringServcie(
    private val cheererRepository: CheererRepository,
    private val cheeringRepository: CheeringRepository,
    private val lifeMapRepository: LifeMapRepository,
    private val userRepository: UserRepository,
    private val cheeringLimiter: CheeringLimiter,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun cheering(request: CheeringRequest) {
        checkCheeringLimit(request.lifeMapId, request.cheererId)

        saveCheerer(request.lifeMapId, request.cheererId)

        applicationEventPublisher.publishEvent(CheeringEvent(request.lifeMapId))

        cheeringLimiter.put(request.lifeMapId, request.cheererId)
    }

    @Transactional(readOnly = true)
    fun findCheeringSquad(lifeMapId: Long, request: CheeringSquadPagingRequest): PageResult<CheererResponse> {
        val cheering = cheeringRepository.findByLifeMapId(lifeMapId)
            ?: Cheering(0, lifeMapId)

        val cheeringSquad =
            findCheeringSquadPage(lifeMapId, request.lastCursorAt, Pageable.ofSize(request.pageSize))

        val isLastPage = isLastPage(cheeringSquad.size, request.pageSize, lifeMapId, cheeringSquad)

        return PageResult.of(cheering.count, cheeringSquad.map(CheererResponse::from), isLastPage)
    }

    @Transactional(readOnly = true)
    fun getCheeringCount(userName: String): CheeringCountResponse {
        val lifeMap = lifeMapRepository.findFirstByUserUsername(userName)
            ?: throw NoSuchElementException("존재하지 않는 유저명입니다. $userName")

        val cheering = cheeringRepository.findByLifeMapId(lifeMap.id!!)

        return CheeringCountResponse.from(cheering)
    }

    @Transactional(readOnly = true)
    fun getCheeringCount(userId: Long): Long {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $userId")

        val cheering = cheeringRepository.findByLifeMapId(lifeMap.id!!)

        return if (cheering == null) {
            0
        } else {
            cheering.count
        }
    }

    private fun saveCheerer(lifeMapId: Long, cheererId: Long) {
        val cheerer = userRepository.getReferenceById(cheererId)

        cheererRepository.save(
            Cheerer(
                lifeMapId = lifeMapId,
                user = cheerer,
                cheeringAt = LocalDateTime.now(),
            ),
        )
    }

    private fun isLastPage(contentSize: Int, pageSize: Int, lifeMapId: Long, cheeringSquad: List<Cheerer>): Boolean {
        return if (contentSize < pageSize) {
            true
        } else {
            !cheererRepository.existsByLifeMapIdAndCheeringAtGreaterThanOrderByCheeringAt(lifeMapId, cheeringSquad.last().cheeringAt)
        }
    }

    private fun findCheeringSquadPage(lifeMapId: Long, cheeringAt: LocalDateTime?, pageable: Pageable): List<Cheerer> {
        return if (cheeringAt == null) {
            cheererRepository.findByLifeMapIdOrderByCheeringAt(lifeMapId, pageable)
        } else {
            cheererRepository.findByLifeMapIdAndCheeringAtGreaterThanOrderByCheeringAt(lifeMapId, cheeringAt, pageable)
        }
    }

    private fun checkCheeringLimit(lifeMapId: Long, cheererId: Long) {
        if (!cheeringLimiter.isPermit(lifeMapId, cheererId)) {
            throw CoreApiException(ErrorInfo.TOO_MANY_CHEERING)
        }
    }
}
