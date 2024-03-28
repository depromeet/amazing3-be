package io.raemian.api.support.response

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
    }
}
