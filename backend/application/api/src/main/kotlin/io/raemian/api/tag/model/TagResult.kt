package io.raemian.api.tag.model

import io.raemian.storage.db.core.tag.Tag

data class TagResult(
    val id: Long?,
    val content: String,
) {

    constructor(tag: Tag) : this(
        tag.id,
        tag.content,
    )
}
