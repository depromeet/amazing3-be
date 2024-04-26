package io.raemian.storage.db.core.common.pagination

import java.util.function.Function

data class PaginationResult<T> internal constructor(
    val contents: List<T>,
    val nextCursor: Long?,
    val isLast: Boolean,
) {
    fun <R> transform(transformer: Function<T, R>): PaginationResult<R> {
        return PaginationResult(
            contents.stream().map(transformer).toList(),
            nextCursor,
            isLast,
        )
    }
}
