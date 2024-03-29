package io.raemian.api.emoji.model

data class EmojiCountSubset(
    val id: Long,
    val name: String,
    val url: String,
    val reactCount: Int,
    val isMyReaction: Boolean,
)
