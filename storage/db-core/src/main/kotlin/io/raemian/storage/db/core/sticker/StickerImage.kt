package io.raemian.storage.db.core.sticker

import jakarta.persistence.Embeddable

@Embeddable
data class StickerImage(
    val stickerImage: String,
)
