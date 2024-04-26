package io.raemian.storage.db.core.cheer

import java.time.LocalDate
import java.time.LocalDateTime

data class GoalExploreQueryResult(
    val goalId: Long,
    val title: String,
    val description: String,
    val deadline: LocalDate,
    val stickerUrl: String,
    val tagContent: String,
    val createdAt: LocalDateTime,
    val lifeMapId: Long,
    val goalCount: Long,
    val historyCount: Long,
    val viewCount: Long,
    val commentCount: Long,
    val userId: Long,
    val nickname: String,
    val username: String,
    val userImage: String,
)
