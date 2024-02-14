package io.raemian.api.auth.controller.request

import java.time.LocalDate

data class UpdateUserRequest(
    val nickname: String,
    val birth: LocalDate,
)

data class UpdateUserInfoRequest(
    val nickname: String,
    val birth: LocalDate,
    val username: String,
    val image: String,
) {
    fun validateUsername(): Boolean {
        val regex = "^[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*".toRegex()
        return regex.matches(username)
    }
}
