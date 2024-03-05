package io.raemian.admin.emoji.controller.request

import org.springframework.web.multipart.MultipartFile

data class UpdateEmojiRequest(
    val name: String,
    val image: MultipartFile,
)
