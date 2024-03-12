package io.raemian.api.event

data class RemovedEmojiEvent(
    val goalId: Long,
    val emojiId: Long,
)
