package io.raemian.adminapi.support.error

class CoreApiException(
    val errorType: ErrorType,
    val data: Any? = null,
) : RuntimeException(errorType.message) {

    override fun fillInStackTrace(): Throwable = this
}
