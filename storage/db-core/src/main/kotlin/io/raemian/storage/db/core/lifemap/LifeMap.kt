package io.raemian.api.lifemap

import io.raemian.storage.db.core.BaseEntity
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.user.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "LIFE_MAPS")
class LifeMap(
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    var isPublic: Boolean = true,

    @OneToMany(
        mappedBy = "lifeMap",
        cascade = [CascadeType.REMOVE, CascadeType.MERGE],
        fetch = FetchType.LAZY,
    )
    var goals: MutableList<Goal> = ArrayList(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {

    fun updatePublic(isPublic: Boolean) {
        this.isPublic = isPublic
    }

    fun addGoal(goal: Goal) {
        this.goals.add(goal)
    }

    fun sortGoals(): List<Goal> {
        return goals.sortedWith(
            compareBy<Goal> { it.deadline }
                .thenByDescending { it.createdAt },
        )
    }
}
