package io.raemian.api.auth.controller.request

import java.time.LocalDate

data class UpdateUserRequest(
    val nickname: String,
    val birth: LocalDate,
    val username: String,
)
