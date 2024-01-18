package io.raemian.api.goal.controller.request

data class CreateGoalRequest(
    val title: String,
    val yearOfDeadline: String,
    val monthOfDeadline: String,
    val stickerId: Long,
    val tagId: Long,
    val description: String? = "",
)
