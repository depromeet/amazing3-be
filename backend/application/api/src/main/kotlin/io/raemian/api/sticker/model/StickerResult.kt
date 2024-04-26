package io.raemian.api.sticker.model

import io.raemian.storage.db.core.sticker.Sticker

data class StickerResult(
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
