package io.raemian.api.emoji.domain

data class EmojiCountSubset(
    val id: Long,
    val name: String,
    val url: String,
    val count: Long,
    val isMine: Boolean,
)
