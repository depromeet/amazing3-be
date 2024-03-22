package io.raemian.storage.db.core.emoji

import org.springframework.data.jpa.repository.JpaRepository

interface EmojiCountRepository : JpaRepository<EmojiCount, Long> {
    fun findByGoalIdAndEmojiId(goalId: Long, emojiId: Long): EmojiCount?
}
