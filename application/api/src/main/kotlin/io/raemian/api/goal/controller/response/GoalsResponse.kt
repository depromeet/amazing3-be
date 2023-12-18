package io.raemian.api.goal.controller.response

import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.sticker.StickerImage
import java.time.LocalDate

fun LocalDate.format(): String {
    var month = this.monthValue.toString()
    if (month.length == 1) {
        month = "0$month"
    }
    
    return "${this.year}.$month"
}

data class GoalsResponse(
    val goals: Goals,
) {

    constructor(goals: List<Goal>) : this(
        Goals(
            goals.map(::GoalInfo),
        ),
    )

    data class GoalInfo(
        val id: Long?,
        val deadline: String,
        val sticker: StickerImage,
        val tagContent: String,
    ) {

        constructor(goal: Goal) : this(
            id = goal.id,
            deadline = goal.deadline.format(),
            sticker = goal.sticker.stickerImage,
            tagContent = goal.tag.content,
        )
    }

    data class Goals(
        val goalInfos: List<GoalInfo>,
    )
}
