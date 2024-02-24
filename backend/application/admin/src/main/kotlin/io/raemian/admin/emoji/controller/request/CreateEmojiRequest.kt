package io.raemian.admin.emoji.controller.request

import org.springframework.web.multipart.MultipartFile
import java.io.Serializable

data class CreateEmojiRequest(
    val name: String,
    val image: MultipartFile,
) : Serializable
