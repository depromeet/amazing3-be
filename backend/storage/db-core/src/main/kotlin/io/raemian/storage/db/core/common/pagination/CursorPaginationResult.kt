package io.raemian.storage.db.core.common.pagination

import java.util.function.Function

data class CursorPaginationResult<CursorType, T> internal constructor(
    val contents: List<T>,
    val nextCursor: CursorType?,
    val isLast: Boolean,
) {
    fun <R> transform(transformer: Function<T, R>): CursorPaginationResult<CursorType, R> {
        return CursorPaginationResult(
            contents.stream().map(transformer).toList(),
            nextCursor,
            isLast,
        )
    }
}
