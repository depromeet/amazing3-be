package io.raemian.api.support.error

open class CoreApiException(
    val errorInfo: ErrorInfo,
) : RuntimeException(errorInfo.message)
