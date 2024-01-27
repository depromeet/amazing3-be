package io.raemian.storage.db.core.lifemap

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "LIFE_MAP_COUNT")
class LifeMapCount(
    val lifeMapId: Long,
    val count: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    fun addCount(): LifeMapCount {
        return LifeMapCount(
            lifeMapId = lifeMapId,
            count = count + 1,
            id = id,
        )
    }
}
