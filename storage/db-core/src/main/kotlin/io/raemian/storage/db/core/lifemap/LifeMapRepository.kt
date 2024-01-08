package io.raemian.storage.db.core.lifemap

import io.raemian.api.lifemap.LifeMap
import org.springframework.data.jpa.repository.JpaRepository

interface LifeMapRepository : JpaRepository<LifeMap, Long> {

    fun findAllByUserId(userId: Long): List<LifeMap>

    fun findAllByUserUserName(userName: String): List<LifeMap>
}
