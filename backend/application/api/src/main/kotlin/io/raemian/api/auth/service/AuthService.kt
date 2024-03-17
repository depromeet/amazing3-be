package io.raemian.api.auth.service

import io.raemian.api.auth.model.CurrentUser
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("not found $username")
        return CurrentUser(
            id = user.id!!,
            email = user.email,
            authorities = listOf(),
        )
    }
}
