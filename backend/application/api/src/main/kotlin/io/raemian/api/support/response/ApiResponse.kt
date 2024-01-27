package io.raemian.api.support.response

import io.raemian.api.support.error.ErrorInfo

class ApiResponse<T> private constructor(
    val result: ResultType,
    val body: T? = null,
    val errorInfo: ErrorInfo? = null,
) {
    companion object {
        fun <S> success(data: S): ApiResponse<S> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }

        fun <S> error(errorInfo: ErrorInfo): ApiResponse<S> {
            return ApiResponse(ResultType.ERROR, null, errorInfo)
        }
    }
}
