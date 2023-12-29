package io.raemian.adminapi.sticker

import io.raemian.adminapi.sticker.controller.request.CreateStickerRequest
import io.raemian.adminapi.sticker.controller.request.UpdateStickerRequest
import io.raemian.adminapi.sticker.controller.response.StickerResponse
import io.raemian.adminapi.support.error.CoreApiException
import io.raemian.adminapi.support.error.ErrorType
import io.raemian.image.enums.FileExtensionType
import io.raemian.image.repository.ImageRepository
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.sticker.StickerRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Service
class StickerService(
    private val stickerRepository: StickerRepository,
    private val imageRepository: ImageRepository
) {

    @Transactional
    fun create(
        createStickerRequest: CreateStickerRequest,
    ): StickerResponse {
        val fileName = validateFileName(createStickerRequest.image.originalFilename)

        val url = imageRepository.upload(fileName, createStickerRequest.image.inputStream)

        val stickers = stickerRepository.save(Sticker(createStickerRequest.name, url))

        return StickerResponse.from(stickers)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<StickerResponse> =
        stickerRepository.findAll().map(::StickerResponse)

    @Transactional
    fun update (
        stickerId: Long,
        updateStickerRequest: UpdateStickerRequest
    ): StickerResponse {
        val newFileName = validateFileName(updateStickerRequest.image.originalFilename)

        val stickers = stickerRepository.getById(stickerId)

        val url = imageRepository.update(
            newFileName,
            splitFileNameFromUrl(stickers.url),
            updateStickerRequest.image.inputStream)

        val updatedStickers = stickerRepository.save(Sticker(updateStickerRequest.name, url))

        return StickerResponse.from(stickers)
    }

    @Transactional
    fun delete(
        stickerId: Long
    ) {
        val stickers = stickerRepository.getById(stickerId)

        imageRepository.delete(splitFileNameFromUrl(stickers.url))

        stickerRepository.delete(stickers)
    }

    private fun splitFileNameFromUrl(url: String): String {
        return url.split("/").last()
    }

    private fun validateFileName(fileName: String?): String {
        if(fileName.isNullOrBlank()) {
            throw CoreApiException(ErrorType.NO_IMAGE_NAME_ERROR)
        }

        if(!fileName.endsWith(FileExtensionType.PNG.value)) {
            throw CoreApiException(ErrorType.NO_PNG_FILE_ERROR)
        }

        return fileName
    }
}
