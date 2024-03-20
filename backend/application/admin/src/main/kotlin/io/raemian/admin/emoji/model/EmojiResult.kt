package io.raemian.admin.emoji.model

import io.raemian.storage.db.core.emoji.Emoji

data class EmojiResult(
    val id: Long?,
    val name: String,
    val url: String,
) {

    companion object {
        fun from(emoji: Emoji): EmojiResult {
            return EmojiResult(emoji.id, emoji.name, emoji.url)
        }
    }
}
