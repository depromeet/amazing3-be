package io.raemian.adminapi.tag

import io.raemian.adminapi.tag.controller.request.CreateTagRequest
import io.raemian.adminapi.tag.controller.request.UpdateTagRequest
import io.raemian.adminapi.tag.controller.response.TagResponse
import io.raemian.storage.db.core.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository,
) {

    @Transactional
    fun create(createTagRequest: CreateTagRequest) : TagResponse =
        TagResponse.from(tagRepository.save(createTagRequest.toEntity()))

    @Transactional(readOnly = true)
    fun findAll(): List<TagResponse> {
        return tagRepository.findAll()
            .map(::TagResponse)
    }

    @Transactional
    fun update(tagId: Long, updateTagRequest: UpdateTagRequest): TagResponse {
        val tags = tagRepository.getById(tagId)
        tags.updateContent(updateTagRequest.content)
        return TagResponse.from(tagRepository.save(tags))
    }

    @Transactional
    fun delete(tagId: Long): Unit {
        val tags = tagRepository.getById(tagId)
        tagRepository.delete(tags)
    }
}
