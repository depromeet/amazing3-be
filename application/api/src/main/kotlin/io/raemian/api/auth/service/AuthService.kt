package io.raemian.api.auth.service

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.auth.domain.UserDTO
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull

@Service
class AuthService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    fun getUserById(id: Long): UserDTO {
        val user = userRepository.getById(id)
        return UserDTO.of(user)
    }

    fun update(id: Long, nickname: String, birth: LocalDate): User {
        val user = userRepository.findById(id)
            .getOrNull() ?: throw RuntimeException("")

        val updated = user.updateInfo(
            nickname = nickname,
            birth = birth,
        )

        return userRepository.save(updated)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("not found $username")
        return CurrentUser(
            id = user.id!!,
            email = user.email,
            authorities = listOf(),
        )
    }
}
