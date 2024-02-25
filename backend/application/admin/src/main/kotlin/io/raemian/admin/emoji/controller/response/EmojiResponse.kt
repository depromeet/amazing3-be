package io.raemian.admin.emoji.controller.response

import io.raemian.storage.db.core.emoji.Emoji

data class EmojiResponse(
    val id: Long?,
    val name: String,
    val url: String,
) {

    companion object {
        fun from(emoji: Emoji): EmojiResponse {
            return EmojiResponse(emoji.id, emoji.name, emoji.url)
        }
    }
}
