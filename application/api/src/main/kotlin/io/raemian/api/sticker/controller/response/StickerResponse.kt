package io.raemian.api.sticker.controller.response

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
}
