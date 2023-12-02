package io.raemian.api.auth.service

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.auth.domain.TokenDTO
import io.raemian.api.support.TokenProvider
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenProvider: TokenProvider,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("not found $username")
        return CurrentUser(
            id = user.id!!,
            email = user.email,
            authorities = listOf(),
        )
    }

    fun signIn(email: String): TokenDTO {
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("아이디 또는 비밀번호 불일치 ")
        val token = UsernamePasswordAuthenticationToken(user.email, "", arrayListOf())
        val authentication = authenticationManagerBuilder.`object`.authenticate(token)
        return tokenProvider.generateTokenDto(authentication)
    }

    fun update(id: Long, nickname: String, birth: LocalDate): User {
        val user = userRepository.findById(id)
            .getOrNull() ?: throw RuntimeException("")

        val copied = user.updateInfo(
            nickname = nickname,
            birth = birth,
        )

        return userRepository.save(copied)
    }
}
