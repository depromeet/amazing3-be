package io.raemian.api.support.response

import io.raemian.api.support.exception.ErrorInfo

class ApiResponse<T> private constructor(
    val result: ResultType,
    val body: T? = null,
    val errorInfo: ErrorInfo? = null,
) {
    companion object {
        fun success(): ApiResponse<Unit> {
            return ApiResponse(ResultType.SUCCESS, null, null)
        }

        fun <S> success(data: S): ApiResponse<S> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }

        fun <S> error(errorInfo: ErrorInfo): ApiResponse<S> {
            return ApiResponse(ResultType.ERROR, null, errorInfo)
        }
    }
}
