package io.raemian.admin.profile

import io.raemian.admin.profile.controller.request.CreateDefaultProfileRequest
import io.raemian.admin.profile.controller.request.UpdateDefaultProfileRequest
import io.raemian.admin.profile.controller.response.DefaultProfileResponse
import io.raemian.admin.support.error.CoreApiException
import io.raemian.admin.support.error.ErrorType
import io.raemian.image.enums.FileExtensionType
import io.raemian.image.repository.ImageRepository
import io.raemian.storage.db.core.profile.DefaultProfile
import io.raemian.storage.db.core.profile.DefaultProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultProfileService(
    private val defaultProfileRepository: DefaultProfileRepository,
    private val imageRepository: ImageRepository,
) {
    private val DEFAULT_PROFILE_FINAL_PATH = "/profile/default"

    @Transactional
    fun create(
        createDefaultProfileRequest: CreateDefaultProfileRequest,
    ): DefaultProfileResponse {
        val fileName = validateFileName(createDefaultProfileRequest.image.originalFilename)

        val url = imageRepository.upload(
            DEFAULT_PROFILE_FINAL_PATH,
            fileName,
            createDefaultProfileRequest.image.inputStream,
        )

        val defaultProflie = defaultProfileRepository.save(DefaultProfile(createDefaultProfileRequest.name, url))

        return DefaultProfileResponse.from(defaultProflie)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<DefaultProfileResponse> =
        defaultProfileRepository.findAll().map(::DefaultProfileResponse)

    @Transactional(readOnly = true)
    fun find(defaultProfileId: Long): DefaultProfileResponse =
        DefaultProfileResponse.from(defaultProfileRepository.getById(defaultProfileId))

    @Transactional
    fun update(
        defaultProfileId: Long,
        updateDefaultProfileRequest: UpdateDefaultProfileRequest,
    ): DefaultProfileResponse {
        val defaultProfile = defaultProfileRepository.getById(defaultProfileId)

        val newFileName = validateFileName(updateDefaultProfileRequest.image.originalFilename)
        val url = imageRepository.update(
            DEFAULT_PROFILE_FINAL_PATH,
            newFileName,
            splitFileNameFromUrl(defaultProfile.url),
            updateDefaultProfileRequest.image.inputStream,
        )

        defaultProfile.updateNameAndUrl(updateDefaultProfileRequest.name, url)

        return DefaultProfileResponse.from(defaultProfileRepository.save(defaultProfile))
    }

    @Transactional
    fun delete(
        stickerId: Long,
    ) {
        val stickers = defaultProfileRepository.getById(stickerId)

        imageRepository.delete(
            DEFAULT_PROFILE_FINAL_PATH,
            splitFileNameFromUrl(stickers.url),
        )

        defaultProfileRepository.delete(stickers)
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
