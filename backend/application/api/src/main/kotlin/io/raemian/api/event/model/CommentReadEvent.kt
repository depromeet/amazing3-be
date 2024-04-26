package io.raemian.api.event.model

import java.time.LocalDateTime

data class CommentReadEvent(
    val goalId: Long,
    val commentReadAt: LocalDateTime,
)
