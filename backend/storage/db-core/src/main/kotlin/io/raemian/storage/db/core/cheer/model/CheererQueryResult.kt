package io.raemian.storage.db.core.cheer.model

import io.raemian.storage.db.core.common.pagination.CursorExtractable
import java.time.LocalDateTime

data class CheererQueryResult(
    val cheererId: Long,
    val userId: Long?,
    val userName: String?,
    val userNickName: String?,
    val userImageUrl: String?,
    val cheeringAt: LocalDateTime,
) : CursorExtractable<Long> {
    override fun cursorId(): Long = cheererId
}
