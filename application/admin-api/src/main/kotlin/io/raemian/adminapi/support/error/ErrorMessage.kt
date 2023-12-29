package io.raemian.adminapi.support.error

data class ErrorMessage private constructor(
    val code: String,
    val message: String,
    val data: Any? = null,
) {
    companion object {
        private val EMPTY_MESSAGE: String = "Empty Message"
    }

    constructor(errorType: ErrorType, data: Any? = null) : this(
        code = errorType.code.name,
        message = errorType.message,
        data = data,
    )

    constructor(errorType: ErrorType, e: Exception) : this(
        code = errorType.code.name,
        message = e.message ?: EMPTY_MESSAGE,
        data = null,
    )
}
