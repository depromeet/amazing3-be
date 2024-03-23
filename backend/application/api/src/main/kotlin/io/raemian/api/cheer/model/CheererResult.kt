package io.raemian.api.cheer.model

import io.raemian.storage.db.core.cheer.model.CheererQueryResult
import java.time.LocalDateTime

data class CheererResult(
    val cheererId: Long,
    val userId: Long,
    val userName: String?,
    val userNickName: String?,
    val userImageUrl: String?,
    val cheeringAt: LocalDateTime,
) {
    companion object {
        fun from(queryResult: CheererQueryResult): CheererResult {
            return CheererResult(
                cheererId = queryResult.cheererId,
                userId = queryResult.userId ?: -1,
                userName = queryResult.userName,
                userNickName = queryResult.userNickName,
                userImageUrl = queryResult.userImageUrl,
                cheeringAt = queryResult.cheeringAt
            )
        }
    }
}
