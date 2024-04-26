package io.raemian.storage.db.core.lifemap

import org.springframework.data.repository.CrudRepository

interface LifeMapCountRepository : CrudRepository<LifeMapCount, Long> {
    fun findByLifeMapId(lifeMapId: Long): LifeMapCount?
}
