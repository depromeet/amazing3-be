package io.raemian.api.tag.service

import io.raemian.api.tag.model.TagResult
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(): List<TagResult> {
        return tagRepository.findAll()
            .map(::TagResult)
    }

    @Transactional(readOnly = true)
    fun getReferenceById(id: Long): Tag {
        return tagRepository.getReferenceById(id)
    }
}
