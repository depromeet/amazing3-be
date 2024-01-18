package io.raemian.api.log.controller.request

import io.raemian.infra.logging.enums.ErrorLocationEnum

data class CreateSlackErrorLogRequest(
    val errorLocation: ErrorLocationEnum,
    val appName: String,
    val errormessage: String,
    val referer: String?,
    val userAgent: String?,
)
