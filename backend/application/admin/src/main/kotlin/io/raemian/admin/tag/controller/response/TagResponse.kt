package io.raemian.admin.tag.controller.response

import io.raemian.storage.db.core.tag.Tag

fun from(entity: Tag): TagResponse =
    TagResponse(entity.id, entity.content)

data class TagResponse(
    val id: Long?,
    val content: String,
) {

    constructor(tag: Tag) : this(
        tag.id,
        tag.content,
    )

    companion object {
        fun from(entity: Tag): TagResponse =
            TagResponse(entity.id, entity.content)
    }
}
