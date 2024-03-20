package io.raemian.api.goal.model

import io.raemian.storage.db.core.model.GoalExploreQueryResult
import java.time.LocalDate
import java.time.LocalDateTime

data class GoalSubset(
    val id: Long,
    val title: String,
    val description: String,
    val deadline: LocalDate,
    val sticker: String,
    val tag: String,
    val createdAt: LocalDateTime,
) {
    constructor(result: GoalExploreQueryResult) : this(
        id = result.goalId,
        title = result.title,
        description = result.description,
        deadline = result.deadline,
        sticker = result.stickerUrl,
        tag = result.tagContent,
        createdAt = result.createdAt,
    )
}
