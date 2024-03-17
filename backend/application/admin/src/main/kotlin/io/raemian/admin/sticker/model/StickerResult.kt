package io.raemian.admin.sticker.model

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

    companion object {
        fun from(entity: Sticker): StickerResult {
            return StickerResult(entity.id, entity.name, entity.url)
        }
    }
}
