package io.raemian.api.goal.controller.response

import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.sticker.StickerImage
import java.time.LocalDate

class GoalsResponse private constructor(
    val goals: List<GoalInfo>,
) {

    companion object {
        fun from(goals: List<Goal>): GoalsResponse =
            GoalsResponse(goals.map(::GoalInfo))
    }

    data class GoalInfo(
        val id: Long?,
        val title: String,
        val deadline: LocalDate,
        val sticker: StickerImage,
        val tagContent: String,
        val description: String? = "",
    ) {

        constructor(goal: Goal) : this(
            id = goal.id,
            title = goal.title,
            deadline = goal.deadline,
            sticker = goal.sticker.stickerImage,
            tagContent = goal.tag.content,
            description = goal.description,
        )
    }
}
