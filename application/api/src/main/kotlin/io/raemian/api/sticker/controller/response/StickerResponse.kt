package io.raemian.api.sticker.controller.response

import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.sticker.StickerImage

data class StickerResponse(
    val id: Long?,
    val name: String,
    val stickerImage: StickerImage,
) {

    constructor(sticker: Sticker) : this(
        sticker.id,
        sticker.name,
        sticker.stickerImage,
    )
}
