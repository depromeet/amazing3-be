package io.raemian.api.comment

data class WriteCommentRequest(
    val goalId: Long,
    val currentUserId: Long,
    val content: String,
)
