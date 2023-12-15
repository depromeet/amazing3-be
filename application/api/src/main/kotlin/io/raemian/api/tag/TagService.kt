package io.raemian.api.tag

import io.raemian.api.tag.controller.response.TagResponse
import io.raemian.storage.db.core.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(): TagResponse {
        return TagResponse(tagRepository.findAll())
    }
}
