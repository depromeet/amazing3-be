package io.raemian.api.task.controller.request

data class RewriteTaskRequest(
    val taskId: Long,
    val newDescription: String,
)
