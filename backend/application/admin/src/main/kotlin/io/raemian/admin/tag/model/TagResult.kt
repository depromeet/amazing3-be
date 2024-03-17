package io.raemian.admin.tag.model

import io.raemian.storage.db.core.tag.Tag

data class TagResult(
    val id: Long?,
    val content: String,
) {

    constructor(tag: Tag) : this(
        tag.id,
        tag.content,
    )

    companion object {
        fun from(entity: Tag): TagResult =
            TagResult(entity.id, entity.content)
    }
}
