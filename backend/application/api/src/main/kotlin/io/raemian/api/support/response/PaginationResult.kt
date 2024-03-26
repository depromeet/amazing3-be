package io.raemian.api.support.response

import io.raemian.storage.db.core.common.pagination.CursorPaginationResult

data class PaginationResult<CursorType, T>(
    val total: Long,
    val contents: List<T>,
    val isLast: Boolean,
    val nextCursor: CursorType?,
) {
    companion object {
        fun <CursorType, T> from(total: Long, result: CursorPaginationResult<CursorType, T>): PaginationResult<CursorType, T> {
            return PaginationResult(
                total = total,
                contents = result.contents,
                isLast = result.isLast,
                nextCursor = result.nextCursor,
            )
        }

        fun <CursorType, T> from(total: Int, result: CursorPaginationResult<CursorType, T>): PaginationResult<CursorType, T> {
            return PaginationResult(
                total = total.toLong(),
                contents = result.contents,
                isLast = result.isLast,
                nextCursor = result.nextCursor,
            )
        }
    }
}
