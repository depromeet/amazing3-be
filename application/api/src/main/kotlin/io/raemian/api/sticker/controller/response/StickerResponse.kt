package io.raemian.api.sticker.controller.response

import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.sticker.StickerImage

data class StickerResponse(
    val stickers: List<StickerInfo>
) {

    constructor(stickers: List<Sticker>) : this(
        stickers.map(::StickerInfo).toList()
    )

    data class StickerInfo(
        val id: Long?,
        val name: String,
        val stickerImage: StickerImage,
    ) {

        constructor(sticker: Sticker) : this(
            sticker.id,
            sticker.name,
            sticker.stickerImage
        )
    }
}
