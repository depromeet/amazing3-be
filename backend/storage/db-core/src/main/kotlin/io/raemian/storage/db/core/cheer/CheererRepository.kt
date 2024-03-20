package io.raemian.storage.db.core.cheer

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CheererRepository : JpaRepository<Cheerer, Long> {

    fun findAllByLifeMapIdAndIdLessThanOrderByIdDesc(lifeMapId: Long, id: Long, pageable: Pageable): List<Cheerer>
}
