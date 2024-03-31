package io.raemian.api.support.security

enum class ProfileType(
    value: String,
) {
    LIVE("live"),
    DEV("dev"),
    LOCAL("local"), ;
    companion object {
        fun fromString(value: String): ProfileType =
            when (value) {
                "live" -> LIVE
                "dev" -> DEV
                else -> LOCAL
            }
    }
}
