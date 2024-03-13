package io.raemian.api.emoji.domain

import io.raemian.storage.db.core.emoji.EmojiCount

data class EmojiCountSubset(
    val id: Long,
    val name: String,
    val url: String,
    val count: Long,
    val isMine: Boolean,
)
