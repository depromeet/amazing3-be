package io.raemian.storage.db.core.lifemap

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "LIFE_MAP_HISTORY")
class LifeMapHistory(
    val lifeMapId: Long,
    val userId: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    companion object {
        fun of(lifeMapId: Long, userId: Long): LifeMapHistory {
            return LifeMapHistory(
                lifeMapId = lifeMapId,
                userId = userId
            )
        }
    }
}