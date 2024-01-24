package io.raemian.admin.profile.controller.request

import org.springframework.web.multipart.MultipartFile

data class UpdateDefaultProfileRequest(
    val name: String,
    val image: MultipartFile,
)
