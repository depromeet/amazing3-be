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
    val goalCount: Long,
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
                goalCount = 0,
            )
        }
    }
    fun addViewCount(): LifeMapCount {
        return LifeMapCount(
            lifeMapId = lifeMapId,
            viewCount = viewCount + 1,
            historyCount = historyCount,
            goalCount = goalCount,
            id = id,
        )
    }

    fun addHistoryCount(): LifeMapCount {
        return LifeMapCount(
            lifeMapId = lifeMapId,
            viewCount = viewCount,
            historyCount = historyCount + 1,
            goalCount = goalCount,
            id = id,
        )
    }

    fun addGoalCount(): LifeMapCount {
        return LifeMapCount(
            lifeMapId = lifeMapId,
            viewCount = viewCount,
            historyCount = historyCount,
            goalCount = goalCount + 1,
            id = id,
        )
    }
}
