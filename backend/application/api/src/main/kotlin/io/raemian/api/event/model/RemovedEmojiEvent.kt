package io.raemian.api.event.model

data class RemovedEmojiEvent(
    val goalId: Long,
    val emojiId: Long,
)
