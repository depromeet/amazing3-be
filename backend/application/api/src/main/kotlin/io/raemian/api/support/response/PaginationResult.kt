package io.raemian.api.support.response

import io.raemian.storage.db.core.common.pagination.CursorPaginationResult

data class PaginationResult<T>(
    val total: Int,
    val contents: List<T>,
    val isLast: Boolean,
    val nextCursor: Long?,
) {
    companion object {
        fun <T> from(total: Int, result: CursorPaginationResult<T>): PaginationResult<T> {
            return PaginationResult(
                total = total,
                contents = result.contents,
                isLast = result.isLast,
                nextCursor = result.nextCursor,
            )
        }
    }
}
