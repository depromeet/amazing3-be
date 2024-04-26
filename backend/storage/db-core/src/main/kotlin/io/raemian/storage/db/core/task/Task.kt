package io.raemian.storage.db.core.task

import io.raemian.storage.db.core.common.BaseEntity
import io.raemian.storage.db.core.goal.Goal
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Nationalized

@Entity
@Table(name = "TASKS")
class Task private constructor(
    @ManyToOne
    @JoinColumn(name = "goal_id")
    val goal: Goal,

    @Column(nullable = false)
    var isDone: Boolean,

    @Column(nullable = false)
    @Nationalized
    var description: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {

    companion object {
        fun createTask(goal: Goal, description: String): Task {
            return Task(
                goal = goal,
                isDone = false,
                description = description,
            )
        }
    }

    fun rewrite(newDescription: String) {
        this.description = newDescription
    }

    fun updateTaskCompletion(isDone: Boolean) {
        this.isDone = isDone
    }
}
