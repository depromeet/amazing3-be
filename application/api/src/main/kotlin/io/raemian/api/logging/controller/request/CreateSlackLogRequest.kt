package io.raemian.api.logging.controller.request

import io.raemian.infra.logging.enums.ErrorLocationEnum

data class CreateSlackErrorLogRequest(
    val errorLocation: ErrorLocationEnum,
    val appName: String,
    val referer: String,
    val userAgent: String,
    val errormessage: String,
)
