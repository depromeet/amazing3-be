package io.raemian.api.emoji.domain

data class EmojiCountSubset(
    val id: Long,
    val name: String,
    val url: String,
    val reactCount: Long,
    val isMyReaction: Boolean,
)
