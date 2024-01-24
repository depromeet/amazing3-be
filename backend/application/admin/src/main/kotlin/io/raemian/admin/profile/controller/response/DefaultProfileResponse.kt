package io.raemian.admin.profile.controller.response

import io.raemian.storage.db.core.profile.DefaultProfile

data class DefaultProfileResponse(
    val id: Long?,
    val name: String,
    val url: String,
) {

    constructor(defaultProfile: DefaultProfile) : this(
        defaultProfile.id,
        defaultProfile.name,
        defaultProfile.url,
    )

    companion object {
        fun from(entity: DefaultProfile): DefaultProfileResponse {
            return DefaultProfileResponse(entity.id, entity.name, entity.url)
        }
    }
}
