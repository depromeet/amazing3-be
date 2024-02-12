package io.raemian.storage.db.core.lifemap

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LifeMapRepository : JpaRepository<LifeMap, Long> {

    fun findFirstByUserId(userId: Long): LifeMap?

    fun findFirstByUserUsername(username: String): LifeMap?

    fun findAllByIdInOrderByIdDesc(ids: List<Long>): List<LifeMap>

}
