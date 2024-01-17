package io.raemian.api.support.error

open class CoreApiException(
    errorInfo: ErrorInfo,
) : RuntimeException(errorInfo.message)
