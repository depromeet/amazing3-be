package io.raemian.storage.db.core.cheer

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CheererRepository : JpaRepository<Cheerer, Long> {

    fun findByLifeMapIdAndCheeringAtLessThanOrderByCheeringAtDesc(lifeMapId: Long, cheeringAt: LocalDateTime, pageable: Pageable): List<Cheerer>

    fun findByLifeMapIdOrderByCheeringAtDesc(lifeMapId: Long, pageable: Pageable): List<Cheerer>

    fun existsByLifeMapIdAndCheeringAtLessThanOrderByCheeringAtDesc(lifeMapId: Long, cheeringAt: LocalDateTime): Boolean
}
