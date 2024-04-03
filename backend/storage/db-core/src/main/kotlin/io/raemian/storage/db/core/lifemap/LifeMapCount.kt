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
    var viewCount: Long,
    var historyCount: Long,
    var goalCount: Long,
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
        this.viewCount += 1
        return this
    }

    fun addHistoryCount(): LifeMapCount {
        this.historyCount += 1
        return this
    }

    fun addGoalCount(): LifeMapCount {
        this.goalCount += 1
        return this
    }

    fun minusGoalCount(): LifeMapCount {
        if (0 < this.goalCount) {
            this.goalCount -= 1
        }
        return this
    }
}
