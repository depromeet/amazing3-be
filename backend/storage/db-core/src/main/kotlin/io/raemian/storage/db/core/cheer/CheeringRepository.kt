package io.raemian.storage.db.core.cheer

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CheeringRepository : JpaRepository<Cheering, Long> {
    fun findByLifeMapId(lifeMapId: Long): Cheering?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cheering c where c.lifeMapId = :lifeMapId")
    fun findByLifeMapIdForUpdate(@Param("lifeMapId") lifeMapId: Long): Cheering?
}
