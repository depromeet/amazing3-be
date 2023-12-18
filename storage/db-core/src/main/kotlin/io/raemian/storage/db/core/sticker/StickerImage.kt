package io.raemian.storage.db.core.sticker

import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.Embeddable

@Embeddable
data class StickerImage(
    @JsonValue
    val stickerImage: String,
)
