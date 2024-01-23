package io.raemian.api.auth.controller.request

import java.time.LocalDate

data class UpdateUserRequest(
    val nickname: String,
    val birth: LocalDate,
)


data class UpdateUserInfoRequest(
    val nickname: String,
    val birth: LocalDate,
    val username: String
)