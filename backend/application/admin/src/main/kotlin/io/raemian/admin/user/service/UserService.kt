package io.raemian.admin.user.service

import io.raemian.admin.user.model.UserResult
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun findAll(): List<UserResult> =
        userRepository.findAll().map(UserResult::from)
}
