package io.raemian.api.goal.controller.response

import io.raemian.api.support.format
import io.raemian.storage.db.core.goal.Goal

class GoalsResponse private constructor(
    val goals: List<GoalInfo>,
    val goalsCount: Int,
) {

    companion object {
        fun from(goals: List<Goal>): GoalsResponse =
            GoalsResponse(
                goals.map(::GoalInfo),
                goals.size,
            )
    }

    data class GoalInfo(
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
}
