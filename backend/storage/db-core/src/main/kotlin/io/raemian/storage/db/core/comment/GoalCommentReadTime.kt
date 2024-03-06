package io.raemian.storage.db.core.comment

import io.raemian.storage.db.core.goal.Goal
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "GOAL_COMMENT_READ_TIMES")
class GoalCommentReadTime(
    @ManyToOne
    @JoinColumn(name = "goal_id", unique = true, nullable = false)
    val goal: Goal,

    var lastCommentReadTime: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    fun updateLastCommentReadTime(commentReadTime: LocalDateTime) {
        this.lastCommentReadTime = commentReadTime
    }
}
