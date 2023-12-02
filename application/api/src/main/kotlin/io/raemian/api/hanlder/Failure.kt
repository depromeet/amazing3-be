package io.raemian.api.hanlder

interface Failure {
    val message: String
}

interface GenericFailure : Failure

data class IllegalArgumentFailure(val exception: Exception) : Failure {
    override val message: String
        get() = exception.localizedMessage

    companion object {
        fun of(message: String): Failure {
            return IllegalArgumentFailure(IllegalArgumentException(message))
        }
    }
}

data class ValidationFailure(override val message: String) : Failure
