package io.raemian.adminapi.sticker.controller.request

import org.springframework.web.multipart.MultipartFile
import java.io.Serializable

data class CreateStickerRequest(
    val name: String,
    val image: MultipartFile,
): Serializable
