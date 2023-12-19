package io.raemian.api.goal.controller.response

import io.raemian.api.support.format
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.sticker.StickerImage

data class CreateGoalResponse(
    val id: Long,
    val title: String,
    val description: String,
    val deadline: String,
    val sticker: StickerImage,
    val tag: String,
) {

    constructor(goal: Goal) : this(
        id = goal.id!!,
        title = goal.title,
        description = goal.description,
        deadline = goal.deadline.format(),
        sticker = goal.sticker.stickerImage,
        tag = goal.tag.content,
    )
}
