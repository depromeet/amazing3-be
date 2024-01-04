package io.raemian.admin.tag

import io.raemian.admin.support.error.CoreApiException
import io.raemian.admin.support.error.ErrorType
import io.raemian.admin.tag.controller.request.CreateTagRequest
import io.raemian.admin.tag.controller.request.UpdateTagRequest
import io.raemian.admin.tag.controller.response.TagResponse
import io.raemian.storage.db.core.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository,
) {

    @Transactional
    fun create(createTagRequest: CreateTagRequest): TagResponse {
        if (tagRepository.existsTagsByContent(createTagRequest.content)) {
            throw CoreApiException(ErrorType.DUPLICATE_TAG_ERROR)
        }

        return TagResponse.from(tagRepository.save(createTagRequest.toEntity()))
    }

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
    fun delete(tagId: Long) {
        val tags = tagRepository.getById(tagId)
        tagRepository.delete(tags)
    }
}
