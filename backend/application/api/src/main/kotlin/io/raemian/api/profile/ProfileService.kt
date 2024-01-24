package io.raemian.api.profile

import io.raemian.api.profile.controller.response.DefaultProfileResponse
import io.raemian.storage.db.core.profile.DefaultProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileService(
    private val defaultProfileRepository: DefaultProfileRepository,
) {
    @Transactional(readOnly = true)
    fun findAllDefault(): List<DefaultProfileResponse> {
        return defaultProfileRepository.findAll()
            .map(::DefaultProfileResponse)
    }
}
