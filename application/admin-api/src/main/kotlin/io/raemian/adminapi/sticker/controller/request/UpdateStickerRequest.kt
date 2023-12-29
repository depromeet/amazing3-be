package io.raemian.adminapi.sticker.controller.request

import org.springframework.web.multipart.MultipartFile

data class UpdateStickerRequest(
    val name: String,
    val image: MultipartFile,
)
