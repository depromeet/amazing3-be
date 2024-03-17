package io.raemian.api.profile.model

import io.raemian.storage.db.core.profile.DefaultProfile

data class DefaultProfileResult(
    val id: Long?,
    val name: String,
    val url: String,
) {

    constructor(sticker: DefaultProfile) : this(
        sticker.id,
        sticker.name,
        sticker.url,
    )
}
