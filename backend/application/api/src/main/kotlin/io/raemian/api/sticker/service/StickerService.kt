package io.raemian.api.sticker.service

import io.raemian.api.sticker.model.StickerResult
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.sticker.StickerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StickerService(
    private val stickerRepository: StickerRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(): List<StickerResult> {
        return stickerRepository.findAll()
            .map(::StickerResult)
    }

    @Transactional(readOnly = true)
    fun getReferenceById(id: Long): Sticker {
        return stickerRepository.getReferenceById(id)
    }
}
