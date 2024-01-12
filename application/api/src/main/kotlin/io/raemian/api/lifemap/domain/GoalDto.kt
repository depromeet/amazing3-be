package io.raemian.api.lifemap.domain

import io.raemian.api.support.format
import io.raemian.storage.db.core.goal.Goal

class GoalDto private constructor(
    val id: Long?,
    val deadline: String,
    val stickerUrl: String,
    val tagContent: String,
) {

    constructor(goal: Goal) : this(
        id = goal.id,
        deadline = goal.deadline.format(),
        stickerUrl = goal.sticker.url,
        tagContent = goal.tag.content,
    )
}
