package io.raemian.api.task.controller.request

data class UpdateTaskCompletionRequest(
    val taskId: Long,
    val isDone: Boolean,
)
