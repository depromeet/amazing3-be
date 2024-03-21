package io.raemian.storage.db.core.emoji

import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface ReactedEmojiRepository : JpaRepository<ReactedEmoji, Long> {
    fun findAllByGoal(goal: Goal): List<ReactedEmoji>

    fun findAllByGoalIdIn(goalIds: List<Long>): List<ReactedEmoji>

    fun deleteByEmojiAndGoalAndReactUser(emoji: Emoji, goal: Goal, reactUser: User)
}
