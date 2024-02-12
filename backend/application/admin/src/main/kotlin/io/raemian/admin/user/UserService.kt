package io.raemian.admin.user

import io.raemian.admin.user.controller.response.UserResponse
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun findAll(): List<UserResponse> =
        userRepository.findAll().map(UserResponse::from)
}
