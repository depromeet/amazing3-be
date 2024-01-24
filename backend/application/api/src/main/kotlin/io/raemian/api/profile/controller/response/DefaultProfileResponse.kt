package io.raemian.api.profile.controller.response

import io.raemian.storage.db.core.profile.DefaultProfile

data class DefaultProfileResponse(
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
