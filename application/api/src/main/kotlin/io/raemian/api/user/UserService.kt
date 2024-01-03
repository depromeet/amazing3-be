package io.raemian.api.user

import io.raemian.api.user.controller.UpdateGoalsPublicRequest
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    fun getById(userId: Long): User = userRepository.getById(userId)

    @Transactional
    fun updateGoalsPublic(userId: Long, updateGoalsPublicRequest: UpdateGoalsPublicRequest) {
        val user = userRepository.getById(userId)
        user.updateGoalsPublic(updateGoalsPublicRequest.isGoalsPublic)
    }
}
