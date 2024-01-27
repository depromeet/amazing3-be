package io.raemian.storage.db.core.lifemap

import org.springframework.data.repository.CrudRepository

interface LifeMapHistoryRepository : CrudRepository<LifeMapHistory, Long> {
    fun findByLifeMapIdAndUserId(lifeMapId: Long, userId: Long): LifeMapHistory?
}
