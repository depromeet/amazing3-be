package io.raemian.storage.db.core.emoji

import jakarta.persistence.Embeddable

@Embeddable
class StickerImage(
    val stickerImage: String,
)
