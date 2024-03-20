package io.raemian.storage.db.core.emoji

import io.raemian.storage.db.core.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "EMOJI_COUNTS", indexes = [Index(name = "IDX_GOAL_ID", columnList = "goalId")])
class EmojiCount(
    @Column
    var count: Long,

    @ManyToOne
    @JoinColumn(name = "emoji_id")
    val emoji: Emoji,

    @Column
    val goalId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {
    fun addCount(): EmojiCount {
        this.count += 1
        return this
    }

    fun minusCount(): EmojiCount {
        this.count -= 1
        return this
    }
}
