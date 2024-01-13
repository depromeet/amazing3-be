package io.raemian.api.goal.controller.response

import io.raemian.api.support.format
import io.raemian.storage.db.core.goal.Goal

data class CreateGoalResponse(
    val id: Long,
    val title: String,
    val description: String,
    val deadline: String,
    val stickerUrl: String,
    val tag: String,
) {

    constructor(goal: Goal) : this(
        id = goal.id!!,
        title = goal.title,
        description = goal.description,
        deadline = goal.deadline.format(),
        stickerUrl = goal.sticker.url,
        tag = goal.tag.content,
    )
}
