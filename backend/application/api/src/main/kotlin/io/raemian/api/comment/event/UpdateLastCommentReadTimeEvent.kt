package io.raemian.api.comment.event

import java.time.LocalDateTime

data class UpdateLastCommentReadTimeEvent(
    val goalId: Long,
    val commentReadTime: LocalDateTime,
)
