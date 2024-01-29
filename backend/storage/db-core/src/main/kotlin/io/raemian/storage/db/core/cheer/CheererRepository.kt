package io.raemian.storage.db.core.cheer

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface CheererRepository: JpaRepository<Cheerer, Long> {
     fun select(lifeMapId: Long, cheeringAt: LocalDateTime?, pageable: Pageable): List<Cheerer> {
        return if (cheeringAt == null)
            findByLifeMapIdOrderByCheeringAtDesc(lifeMapId, pageable)
        else
            findByLifeMapIdAndCreatedAtGreaterThanOrderByCheeringAtDesc(lifeMapId, cheeringAt, pageable);
    }

    fun findByLifeMapIdAndCreatedAtGreaterThanOrderByCheeringAtDesc(lifeMapId: Long, cheeringAt: LocalDateTime, pageable: Pageable): List<Cheerer>
    fun findByLifeMapIdOrderByCheeringAtDesc(lifeMapId: Long, pageable: Pageable): List<Cheerer>
}