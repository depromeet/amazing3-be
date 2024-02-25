package io.raemian.admin.emoji

import io.raemian.admin.emoji.controller.request.CreateEmojiRequest
import io.raemian.admin.emoji.controller.request.UpdateEmojiRequest
import io.raemian.admin.emoji.controller.response.EmojiResponse
import io.raemian.admin.support.error.CoreApiException
import io.raemian.admin.support.error.ErrorType
import io.raemian.image.enums.FileExtensionType
import io.raemian.image.repository.ImageRepository
import io.raemian.storage.db.core.emoji.Emoji
import io.raemian.storage.db.core.emoji.EmojiRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmojiService(
    private val emojiRepository: EmojiRepository,
    private val imageRepository: ImageRepository,
) {
    private val EMOJI_FINAL_PATH = "/emoji"

    @Transactional
    fun create(
        createEmojiRequest: CreateEmojiRequest,
    ): EmojiResponse {
        val fileName = createEmojiRequest.image.originalFilename
        validateFileName(fileName)

        val url = imageRepository.upload(
            finalPath = EMOJI_FINAL_PATH,
            fileName = fileName!!,
            inputStream = createEmojiRequest.image.inputStream,
        )

        val emoji = Emoji(createEmojiRequest.name, url)
        return EmojiResponse.from(emojiRepository.save(emoji))
    }

    @Transactional(readOnly = true)
    fun findAll(): List<EmojiResponse> =
        emojiRepository.findAll().map(EmojiResponse::from)

    @Transactional(readOnly = true)
    fun find(emojiId: Long): EmojiResponse =
        EmojiResponse.from(emojiRepository.getById(emojiId))

    @Transactional
    fun update(
        emojiId: Long,
        updateEmojiRequest: UpdateEmojiRequest,
    ): EmojiResponse {
        val newFileName = updateEmojiRequest.image.originalFilename
        validateFileName(newFileName)

        val emoji = emojiRepository.getById(emojiId)
        val url = imageRepository.update(
            finalPath = EMOJI_FINAL_PATH,
            newFileName = newFileName!!,
            oldFileName = splitFileNameFromUrl(emoji.url),
            inputStream = updateEmojiRequest.image.inputStream,
        )

        emoji.updateNameAndUrl(updateEmojiRequest.name, url)
        return EmojiResponse.from(emojiRepository.save(emoji))
    }

    @Transactional
    fun delete(
        emojiId: Long,
    ) {
        val emoji = emojiRepository.getById(emojiId)

        imageRepository.delete(
            finalPath = EMOJI_FINAL_PATH,
            fileName = splitFileNameFromUrl(emoji.url),
        )
        emojiRepository.delete(emoji)
    }

    private fun splitFileNameFromUrl(url: String): String = url.split("/").last()

    private fun validateFileName(fileName: String?) {
        if (fileName.isNullOrBlank()) {
            throw CoreApiException(ErrorType.NO_IMAGE_NAME_ERROR)
        }

        if (!fileName.endsWith(FileExtensionType.PNG.value)) {
            throw CoreApiException(ErrorType.NO_PNG_FILE_ERROR)
        }
    }
}
