package io.raemian.api.task.controller.request

data class CreateTaskRequest(
    val goalId: Long,
    val description: String
)
