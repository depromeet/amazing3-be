package io.raemian.storage.db.core.sticker

import jakarta.persistence.Embeddable

@Embeddable
class StickerImage(
    val stickerImage: String,
)
