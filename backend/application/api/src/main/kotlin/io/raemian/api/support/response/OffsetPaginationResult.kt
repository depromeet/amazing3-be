package io.raemian.api.support.response

import java.util.Collections

data class OffsetPaginationResult<T>(
    val total: Long,
    val page: Int,
    val size: Int,
    val contents: List<T>,
) {
    companion object {
        fun <T> of(page: Int, size: Int, total: Long, contents: List<T>): OffsetPaginationResult<T> {
            return OffsetPaginationResult(total, page, size, contents)
        }

        fun <T> empty(page: Int, size: Int): OffsetPaginationResult<T> {
            return OffsetPaginationResult(0L, page, size, Collections.emptyList())
        }
    }
}
