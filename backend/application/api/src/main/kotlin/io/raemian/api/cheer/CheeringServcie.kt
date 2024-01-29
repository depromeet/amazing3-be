package io.raemian.api.cheer

import io.raemian.api.cheer.controller.request.CheeringSquadPagingRequest
import io.raemian.api.cheer.controller.response.CheererResponse
import io.raemian.api.support.response.PageResult
import io.raemian.storage.db.core.cheer.Cheerer
import io.raemian.storage.db.core.cheer.CheererRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CheeringServcie(
    private val cheererRepository: CheererRepository,
) {

    fun findCheeringSquad(lifeMapId: Long, request: CheeringSquadPagingRequest): PageResult<CheererResponse> {
        val cheeringSquad = findCheeringSquadPage(lifeMapId, request.lastCursorAt, Pageable.ofSize(request.pageSize))

        val isLastPage = isLastPage(cheeringSquad.isEmpty(), lifeMapId, request.lastCursorAt, cheeringSquad)

        return PageResult.of(cheeringSquad.map(::CheererResponse), isLastPage)
    }

    fun isLastPage(isEmptyContents: Boolean, lifeMapId: Long, lastCursorAt: LocalDateTime?, cheeringSquad: List<Cheerer>): Boolean {
        return if (isEmptyContents) {
            true
        } else {
            !cheererRepository.existsByLifeMapIdAndCheeringAtGreaterThan(lifeMapId, lastCursorAt ?: cheeringSquad.last().cheeringAt)
        }
    }

    fun findCheeringSquadPage(lifeMapId: Long, cheeringAt: LocalDateTime?, pageable: Pageable): List<Cheerer> {
        return if (cheeringAt == null) {
            cheererRepository.findByLifeMapIdOrderByCheeringAtDesc(lifeMapId, pageable)
        } else {
            cheererRepository.findByLifeMapIdAndCheeringAtGreaterThanOrderByCheeringAtDesc(lifeMapId, cheeringAt, pageable);
        }
    }
}