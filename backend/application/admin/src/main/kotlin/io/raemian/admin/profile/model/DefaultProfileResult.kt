package io.raemian.admin.profile.model

import io.raemian.storage.db.core.profile.DefaultProfile

data class DefaultProfileResult(
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
        fun from(entity: DefaultProfile): DefaultProfileResult {
            return DefaultProfileResult(entity.id, entity.name, entity.url)
        }
    }
}
