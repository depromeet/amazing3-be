package io.raemian.api.support.response

data class NewPaginationResult<T>(
    val page: Int,
    val size: Int,
    val total: Long,
    val contents: List<T>,
) {
    companion object {
        fun <T> of(page: Int, size: Int, total: Long, contents: List<T>): NewPaginationResult<T> {
            return NewPaginationResult(page, size, total, contents)
        }
    }
}
