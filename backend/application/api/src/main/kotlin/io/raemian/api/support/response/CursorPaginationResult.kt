package io.raemian.api.support.response

import io.raemian.storage.db.core.common.pagination.PaginationResult

data class CursorPaginationResult<T>(
    val total: Long,
    val contents: List<T>,
    val isLast: Boolean,
    val nextCursor: Long?,
) {
    companion object {
        fun <T> from(total: Long, result: PaginationResult<T>): CursorPaginationResult<T> {
            return CursorPaginationResult(
                total = total,
                contents = result.contents,
                isLast = result.isLast,
                nextCursor = result.nextCursor,
            )
        }

        fun <T> from(total: Int, result: PaginationResult<T>): CursorPaginationResult<T> {
            return CursorPaginationResult(
                total = total.toLong(),
                contents = result.contents,
                isLast = result.isLast,
                nextCursor = result.nextCursor,
            )
        }
    }
}
