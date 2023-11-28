package io.raemian.api.auth.service

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.auth.domain.TokenDTO
import io.raemian.api.support.TokenProvider
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenProvider: TokenProvider,
) : UserDetailsService {

    fun save(email: String, password: String) {
        userRepository.save(
            User(
                email = email,
                password = passwordEncoder.encode(password),
                authority = Authority.ROLE_USER,
            ),
        )
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("not found $username")
        return CurrentUser(
            id = user.id!!,
            email = user.email,
            password = user.password,
            authorities = listOf(),
        )
    }

    fun signIn(email: String, password: String): TokenDTO {
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("아이디 또는 비밀번호 불일치 ")
        if (!passwordEncoder.matches(password, user.password)) {
            throw RuntimeException("아이디 또는 비밀번호 불일치 ")
        }

        val token = UsernamePasswordAuthenticationToken(email, password, arrayListOf())
        val authentication = authenticationManagerBuilder.`object`.authenticate(token)
        val tokenDTO = tokenProvider.generateTokenDto(authentication)

        return tokenDTO
    }
}
