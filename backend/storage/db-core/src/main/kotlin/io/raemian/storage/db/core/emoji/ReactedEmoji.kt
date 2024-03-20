package io.raemian.storage.db.core.emoji

import io.raemian.storage.db.core.common.BaseEntity
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.user.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "REACTED_EMOJIS",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_reacted_emoji", columnNames = ["goal_id", "react_user_id", "emoji_id"]),
    ],
)
class ReactedEmoji(
    @ManyToOne
    @JoinColumn(name = "goal_id")
    val goal: Goal,

    @ManyToOne
    @JoinColumn(name = "emoji_id")
    val emoji: Emoji,

    @ManyToOne
    @JoinColumn(name = "react_user_id")
    val reactUser: User,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity()
