package io.raemian.api.support.error

open class CoreApiException(
    val errorInfo: ErrorInfo,
    errorMessage: String? = errorInfo.message,
) : RuntimeException(errorMessage)
