package io.raemian.api.cheer.service

import io.raemian.api.cheer.controller.request.CheeringRequest
import io.raemian.api.cheer.controller.request.CheeringSquadPageRequest
import io.raemian.api.cheer.model.CheeringCountResult
import io.raemian.api.event.model.CheeredEvent
import io.raemian.api.support.exception.CoreApiException
import io.raemian.api.support.exception.ErrorInfo
import io.raemian.api.support.limiter.CheeringLimiter
import io.raemian.api.support.response.PaginationResult
import io.raemian.storage.db.core.cheer.CheerJdbcQueryRepository
import io.raemian.storage.db.core.cheer.Cheerer
import io.raemian.storage.db.core.cheer.CheererRepository
import io.raemian.storage.db.core.cheer.Cheering
import io.raemian.storage.db.core.cheer.CheeringRepository
import io.raemian.storage.db.core.cheer.model.CheererQueryResult
import io.raemian.storage.db.core.common.pagination.CursorPaginationResult
import io.raemian.storage.db.core.common.pagination.CursorPaginationTemplate
import io.raemian.storage.db.core.lifemap.LifeMapRepository
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CheeringService(
    private val cheeringLimiter: CheeringLimiter,
    private val cheererJdbcQueryRepository: CheerJdbcQueryRepository,
    private val cheererRepository: CheererRepository,
    private val cheeringRepository: CheeringRepository,
    private val lifeMapRepository: LifeMapRepository,
    private val userRepository: UserRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun cheering(request: CheeringRequest) {
        checkCheeringLimit(request.lifeMapId, request.cheererId)

        saveCheerer(request.lifeMapId, request.cheererId)

        applicationEventPublisher.publishEvent(CheeredEvent(request.lifeMapId))

        cheeringLimiter.put(request.lifeMapId, request.cheererId)
    }

    @Transactional(readOnly = true)
    fun findCheeringSquad(lifeMapId: Long, request: CheeringSquadPageRequest): PaginationResult<CheererQueryResult> {
        val cheering = cheeringRepository.findByLifeMapId(lifeMapId)
            ?: Cheering(0, lifeMapId)

        val cheeringSquad = findCheeringSquadWithCursor(lifeMapId, request)

        return PaginationResult.from(cheering.count, cheeringSquad)
    }

    @Transactional(readOnly = true)
    fun getCheeringCount(userName: String): CheeringCountResult {
        val lifeMap = lifeMapRepository.findFirstByUserUsername(userName)
            ?: throw NoSuchElementException("존재하지 않는 유저명입니다. $userName")

        val cheering = cheeringRepository.findByLifeMapId(lifeMap.id!!)

        return CheeringCountResult.from(cheering)
    }

    @Transactional(readOnly = true)
    fun getCheeringCount(userId: Long): Long {
        val lifeMap = lifeMapRepository.findFirstByUserId(userId)
            ?: throw NoSuchElementException("존재하지 않는 유저입니다. $userId")

        val cheering = cheeringRepository.findByLifeMapId(lifeMap.id!!)

        return cheering?.count ?: 0
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

    private fun findCheeringSquadWithCursor(lifeMapId: Long, request: CheeringSquadPageRequest): CursorPaginationResult<CheererQueryResult> {
        return CursorPaginationTemplate.execute(lifeMapId, request.cursor ?: Long.MAX_VALUE, request.size) {
                id, cursor, size ->
            cheererJdbcQueryRepository.findAllByLifeMapWithCursor(id, cursor, size)
        }
    }

    private fun checkCheeringLimit(lifeMapId: Long, cheererId: Long) {
        if (!cheeringLimiter.isPermit(lifeMapId, cheererId)) {
            throw CoreApiException(ErrorInfo.TOO_MANY_CHEERING)
        }
    }
}
