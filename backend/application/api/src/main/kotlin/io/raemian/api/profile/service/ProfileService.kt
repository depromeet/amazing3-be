package io.raemian.api.profile.service

import io.raemian.api.profile.model.DefaultProfileResult
import io.raemian.storage.db.core.profile.DefaultProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileService(
    private val defaultProfileRepository: DefaultProfileRepository,
) {
    @Transactional(readOnly = true)
    fun findAllDefault(): List<DefaultProfileResult> {
        return defaultProfileRepository.findAll()
            .map(::DefaultProfileResult)
    }
}
