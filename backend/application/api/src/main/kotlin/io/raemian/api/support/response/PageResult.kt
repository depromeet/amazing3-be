package io.raemian.api.support.response

class PageResult<T> private constructor(
    val contents: List<T>,
    val isLastPage: Boolean
) {
    companion object {
        fun <T> of(contents: List<T>, isLastPage: Boolean): PageResult<T> {
            return PageResult<T>(contents, isLastPage)
        }
    }
}