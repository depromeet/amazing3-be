package io.raemian.storage.db.core.cheer

import org.springframework.data.jpa.repository.JpaRepository

interface CheeringRepository : JpaRepository<Cheering, Long> {
    fun findByLifeMapId(lifeMapId: Long): Cheering?
}
