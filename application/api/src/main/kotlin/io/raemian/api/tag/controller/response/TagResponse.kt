package io.raemian.api.tag.controller.response

import io.raemian.storage.db.core.tag.Tag

data class TagResponse(
    val tags: List<TagInfo>,
) {

    constructor(tags: List<Tag>) : this(
        tags.map(::TagInfo).toList(),
    )

    data class TagInfo(
        val id: Long?,
        val content: String,
    ) {

        constructor(tag: Tag) : this(
            tag.id,
            tag.content,
        )
    }
}
