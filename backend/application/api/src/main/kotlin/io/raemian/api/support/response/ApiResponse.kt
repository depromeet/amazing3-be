package io.raemian.api.support.response

import io.raemian.api.support.error.ErrorMessage
import io.raemian.api.support.error.ErrorType

class ApiResponse<T> private constructor(
    val result: ResultType,
    val body: T? = null,
    val error: ErrorMessage? = null,
) {
    companion object {
        fun <S> success(data: S): ApiResponse<S> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }

        fun <S> error(error: ErrorType, errorData: Any? = null): ApiResponse<S> {
            return ApiResponse(ResultType.ERROR, null, ErrorMessage(error, errorData))
        }
    }
}
