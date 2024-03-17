package io.raemian.api.lifemap.domain

import io.raemian.api.support.format
import io.raemian.storage.db.core.goal.Goal

class GoalDto private constructor(
    val id: Long?,
    val title: String,
    @Deprecated("필드 삭제 예정")
    val deadline: String,
    val stickerUrl: String,
    @Deprecated("필드 삭제 예정")
    val tagContent: String,
) {

    constructor(goal: Goal) : this(
        id = goal.id,
        title = goal.title,
        deadline = goal.deadline.format(),
        stickerUrl = goal.sticker.url,
        tagContent = goal.tag.content,
    )
}
