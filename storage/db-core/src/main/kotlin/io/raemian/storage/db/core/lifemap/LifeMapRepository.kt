package io.raemian.storage.db.core.lifemap

import io.raemian.api.lifemap.LifeMap
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface LifeMapRepository : JpaRepository<LifeMap, Long> {

    fun findFirstByUserId(userId: Long): Optional<LifeMap>

    fun findFirstByUserUserName(userName: String): Optional<LifeMap>
}
