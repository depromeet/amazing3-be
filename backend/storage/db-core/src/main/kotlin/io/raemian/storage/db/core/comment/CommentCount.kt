package io.raemian.storage.db.core.comment

import io.raemian.storage.db.core.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(name = "COMMENT_COUNTS", indexes = [Index(name = "IDX_GOAL_ID", columnList = "goalId")])
class CommentCount(
    @Column
    var count: Long,

    @Column
    val goalId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
): BaseEntity() {
    fun addCount(): CommentCount {
        this.count += 1
        return this
    }

    fun minusCount(): CommentCount {
        this.count -= 1
        return this
    }
}