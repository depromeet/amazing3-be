package io.raemian.api.sticker

import io.raemian.api.sticker.controller.response.StickerResponse
import io.raemian.storage.db.core.sticker.StickerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StickerService(
    private val stickerRepository: StickerRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(): StickerResponse {
        return StickerResponse(stickerRepository.findAll())
    }
}
