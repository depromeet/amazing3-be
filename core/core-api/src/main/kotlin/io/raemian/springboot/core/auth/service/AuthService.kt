package io.raemian.springboot.core.auth.service

import io.raemian.springboot.core.auth.domain.SecurityUser
import io.raemian.springboot.core.auth.domain.TokenDTO
import io.raemian.springboot.core.auth.support.TokenProvider
import io.raemian.springboot.storage.db.core.user.Authority
import io.raemian.springboot.storage.db.core.user.User
import io.raemian.springboot.storage.db.core.user.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
                authority = Authority.USER,
            ),
        )
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("not found $username")
        return SecurityUser(username = user.email, password = user.password, authority = "USER")
    }

    fun signIn(email: String, password: String): TokenDTO {
        val token = UsernamePasswordAuthenticationToken(email, password, arrayListOf(SimpleGrantedAuthority("USER")))
        val authentication = authenticationManagerBuilder.`object`.authenticate(token)

        val tokenDTO = tokenProvider.generateTokenDto(authentication)


        return tokenDTO
    }
}