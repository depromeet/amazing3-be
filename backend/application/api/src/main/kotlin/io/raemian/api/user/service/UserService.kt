package io.raemian.api.user.service

import io.raemian.api.auth.domain.UserDTO
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserDTO {
        val user = userRepository.getById(id)
        return UserDTO.of(user)
    }

    fun updateNicknameAndBirth(id: Long, nickname: String, birth: LocalDate?): UserDTO {
        val user = userRepository.getById(id)

        val updated = user.updateNicknameAndBirth(
            nickname = nickname,
            birth = birth,
        )

        return UserDTO.of(userRepository.save(updated))
    }

    fun updateBaseInfo(id: Long, nickname: String, birth: LocalDate?, image: String): UserDTO {
        val user = userRepository.getById(id)

        val updated = user.updateNicknameAndBirth(
            nickname = nickname,
            birth = birth,
        ).updateImage(image)

        return UserDTO.of(userRepository.save(updated))
    }

    fun update(id: Long, nickname: String, birth: LocalDate?, username: String, image: String): UserDTO {
        val user = userRepository.getById(id)

        val updated = user
            .updateNicknameAndBirth(
                nickname = nickname,
                birth = birth,
            )
            .updateUsername(username)
            .updateImage(image)

        return UserDTO.of(userRepository.save(updated))
    }

    fun delete(id: Long) {
        userRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun isDuplicatedUsername(username: String): Boolean {
        userRepository.findByUsername(username) ?: return false
        return true
    }
}
