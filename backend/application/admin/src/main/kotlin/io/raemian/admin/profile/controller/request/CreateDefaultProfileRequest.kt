package io.raemian.admin.profile.controller.request

import org.springframework.web.multipart.MultipartFile
import java.io.Serializable

data class CreateDefaultProfileRequest(
    val name: String,
    val image: MultipartFile,
) : Serializable
