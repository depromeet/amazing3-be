package io.raemian.storage.db.core.goal.model

import io.raemian.storage.db.core.common.pagination.CursorExtractable
import java.time.LocalDateTime

data class GoalQueryResult(
    val goalId: Long,
    val title: String,
    val description: String,
    val deadline: LocalDateTime,
    val stickerUrl: String,
    val tag: String,
    val createdAt: LocalDateTime,
) : CursorExtractable {
    override fun getCursorId(): Long = goalId
}
