package io.raemian.api.goal.controller.response

import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.sticker.StickerImage
import java.time.LocalDate

data class GoalsResponse(
    val goals: Goals,
) {

    constructor(goals: List<Goal>) : this(
        Goals(
            goals.map(::GoalInfo)
                .toList()),
    )

    data class GoalInfo(
        val id: Long?,
        val title: String,
        val deadline: LocalDate,
        val sticker: StickerImage,
        val tagContent: String,
        val description: String,
    ) {

        constructor(goal: Goal) : this(
            goal.id,
            goal.title,
            goal.deadline,
            goal.sticker.stickerImage,
            goal.tag.content,
            goal.description,
        )
    }

    data class Goals(
        val goalInfos: List<GoalInfo>,
    )
}
