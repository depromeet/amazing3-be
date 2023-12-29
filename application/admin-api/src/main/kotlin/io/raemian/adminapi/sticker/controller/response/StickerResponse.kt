package io.raemian.adminapi.sticker.controller.response

import io.raemian.storage.db.core.sticker.Sticker

data class StickerResponse(
    val id: Long?,
    val name: String,
    val url: String,
) {

    constructor(sticker: Sticker) : this(
        sticker.id,
        sticker.name,
        sticker.url,
    )

    companion object {
        fun from(entity: Sticker): StickerResponse {
            return StickerResponse(entity.id, entity.name, entity.url)
        }
    }
}
