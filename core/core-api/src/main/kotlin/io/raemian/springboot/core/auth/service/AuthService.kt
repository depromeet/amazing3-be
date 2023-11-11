package io.raemian.springboot.core.auth.service

import io.raemian.springboot.core.auth.domain.SecurityUser
import io.raemian.springboot.storage.db.core.user.User
import io.raemian.springboot.storage.db.core.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {

    fun save(email: String, password: String) {
        userRepository.save(
            User(
                email = email,
                password = passwordEncoder.encode(password),
            ),
        )
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("not found $username")
        return SecurityUser(username = user.email, password = user.password)
    }

    fun signIn(email: String, password: String) {

    }
}