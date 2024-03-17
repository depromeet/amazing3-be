package io.raemian.admin.tag.service

import io.raemian.admin.support.error.CoreApiException
import io.raemian.admin.support.error.ErrorType
import io.raemian.admin.tag.controller.request.CreateTagRequest
import io.raemian.admin.tag.controller.request.UpdateTagRequest
import io.raemian.admin.tag.model.TagResult
import io.raemian.storage.db.core.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository,
) {

    @Transactional
    fun create(createTagRequest: CreateTagRequest): TagResult {
        if (tagRepository.existsTagsByContent(createTagRequest.content)) {
            throw CoreApiException(ErrorType.DUPLICATE_TAG_ERROR)
        }

        return TagResult.from(tagRepository.save(createTagRequest.toEntity()))
    }

    @Transactional(readOnly = true)
    fun findAll(): List<TagResult> =
        tagRepository.findAll().map(::TagResult)

    @Transactional(readOnly = true)
    fun find(tagId: Long): TagResult =
        TagResult.from(tagRepository.getById(tagId))

    @Transactional
    fun update(tagId: Long, updateTagRequest: UpdateTagRequest): TagResult {
        val tags = tagRepository.getById(tagId)
        tags.updateContent(updateTagRequest.content)
        return TagResult.from(tagRepository.save(tags))
    }

    @Transactional
    fun delete(tagId: Long) {
        val tags = tagRepository.getById(tagId)
        tagRepository.delete(tags)
    }
}
