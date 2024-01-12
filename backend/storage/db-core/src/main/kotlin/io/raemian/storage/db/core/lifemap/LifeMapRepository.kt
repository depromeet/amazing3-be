package io.raemian.storage.db.core.lifemap

import io.raemian.api.lifemap.LifeMap
import org.springframework.data.jpa.repository.JpaRepository

interface LifeMapRepository : JpaRepository<LifeMap, Long> {

    fun findFirstByUserId(userId: Long): LifeMap?

    fun findFirstByUserUsername(username: String): LifeMap?
}
