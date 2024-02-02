package io.raemian.api.support.response

class PageResult<T> private constructor(
    val total: Long,
    val contents: List<T>,
    val isLastPage: Boolean,
) {
    companion object {
        fun <T> of(total: Long, contents: List<T>, isLastPage: Boolean): PageResult<T> {
            return PageResult<T>(total, contents, isLastPage)
        }
    }
}
