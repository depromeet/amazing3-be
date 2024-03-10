package io.raemian.api.event

data class RemoveEmojiEvent(
    val goalId: Long,
    val emojiId: Long,
)
