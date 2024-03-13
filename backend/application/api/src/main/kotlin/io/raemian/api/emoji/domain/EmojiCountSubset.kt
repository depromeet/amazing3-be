package io.raemian.api.emoji.domain

import io.raemian.storage.db.core.emoji.EmojiCount

data class EmojiCountSubset(
    val id: Long,
    val name: String,
    val url: String,
    val count: Long,
    val isMine: Boolean
) {
    constructor(emojiCount: EmojiCount): this(
        id = emojiCount.emoji.id ?: -1,
        name = emojiCount.emoji.name,
        url = emojiCount.emoji.url,
        count = emojiCount.count,
        isMine = false
    )
}
