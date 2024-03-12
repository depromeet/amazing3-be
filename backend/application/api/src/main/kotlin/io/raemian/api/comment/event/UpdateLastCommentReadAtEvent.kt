package io.raemian.api.comment.event

import java.time.LocalDateTime

data class UpdateLastCommentReadAtEvent(
    val goalId: Long,
    val commentReadAt: LocalDateTime,
)
