package io.raemian.api.support.exception

open class CoreApiException(
    val errorInfo: ErrorInfo,
    errorMessage: String? = errorInfo.message,
) : RuntimeException(errorMessage)
