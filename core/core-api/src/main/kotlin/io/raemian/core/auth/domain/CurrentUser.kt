package io.raemian.core.auth.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class CurrentUser(
    val id: Long,
    val email: String,
    private val password: String,
    private val authorities: List<String> = listOf("ROLE_USER"),
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        authorities
            .map { SimpleGrantedAuthority(it) }
            .toMutableList()


    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

}