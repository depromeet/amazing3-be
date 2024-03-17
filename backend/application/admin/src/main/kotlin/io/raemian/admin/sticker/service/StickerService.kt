package io.raemian.admin.sticker.service

import io.raemian.admin.sticker.controller.request.CreateStickerRequest
import io.raemian.admin.sticker.controller.request.UpdateStickerRequest
import io.raemian.admin.sticker.model.StickerResult
import io.raemian.admin.support.error.CoreApiException
import io.raemian.admin.support.error.ErrorType
import io.raemian.image.enums.FileExtensionType
import io.raemian.image.repository.ImageRepository
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.sticker.StickerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StickerService(
    private val stickerRepository: StickerRepository,
    private val imageRepository: ImageRepository,
) {
    private val STICKER_FINAL_PATH = "/sticker"

    @Transactional
    fun create(
        createStickerRequest: CreateStickerRequest,
    ): StickerResult {
        val fileName = validateFileName(createStickerRequest.image.originalFilename)

        val url = imageRepository.upload(
            STICKER_FINAL_PATH,
            fileName,
            createStickerRequest.image.inputStream,
        )

        val sticker = stickerRepository.save(Sticker(createStickerRequest.name, url))

        return StickerResult.from(sticker)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<StickerResult> =
        stickerRepository.findAll().map(::StickerResult)

    @Transactional(readOnly = true)
    fun find(stickerId: Long): StickerResult =
        StickerResult.from(stickerRepository.getById(stickerId))

    @Transactional
    fun update(
        stickerId: Long,
        updateStickerRequest: UpdateStickerRequest,
    ): StickerResult {
        val sticker = stickerRepository.getById(stickerId)

        val newFileName = validateFileName(updateStickerRequest.image.originalFilename)
        val url = imageRepository.update(
            STICKER_FINAL_PATH,
            newFileName,
            splitFileNameFromUrl(sticker.url),
            updateStickerRequest.image.inputStream,
        )

        sticker.updateNameAndUrl(updateStickerRequest.name, url)

        val updatedSticker = stickerRepository.save(sticker)

        return StickerResult.from(updatedSticker)
    }

    @Transactional
    fun delete(
        stickerId: Long,
    ) {
        val sticker = stickerRepository.getById(stickerId)

        imageRepository.delete(
            STICKER_FINAL_PATH,
            splitFileNameFromUrl(sticker.url),
        )

        stickerRepository.delete(sticker)
    }

    private fun splitFileNameFromUrl(url: String): String {
        return url.split("/").last()
    }

    private fun validateFileName(fileName: String?): String {
        if (fileName.isNullOrBlank()) {
            throw CoreApiException(ErrorType.NO_IMAGE_NAME_ERROR)
        }

        if (!fileName.endsWith(FileExtensionType.PNG.value)) {
            throw CoreApiException(ErrorType.NO_PNG_FILE_ERROR)
        }

        return fileName
    }
}
