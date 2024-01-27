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
    val viewCount: Long,
    val historyCount: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    companion object {
        fun of(lifeMapId: Long): LifeMapCount {
            return LifeMapCount(
                lifeMapId = lifeMapId,
                viewCount = 0,
                historyCount = 0,
            )
        }
    }
    fun addViewCount(): LifeMapCount {
        return LifeMapCount(
            lifeMapId = lifeMapId,
            viewCount = viewCount + 1,
            historyCount = historyCount,
            id = id,
        )
    }

    fun addHistoryCount(): LifeMapCount {
        return LifeMapCount(
            lifeMapId = lifeMapId,
            viewCount = viewCount,
            historyCount = historyCount + 1,
            id = id,
        )
    }

    fun addCount(): LifeMapCount {
        return LifeMapCount(
            lifeMapId = lifeMapId,
            viewCount = viewCount + 1,
            historyCount = historyCount + 1,
            id = id,
        )
    }
}
