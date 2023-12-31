package io.raemian.admin.tag.controller.request

import io.raemian.storage.db.core.tag.Tag

data class CreateTagRequest(
    val content: String,
) {
    fun toEntity() = Tag(content)
}
