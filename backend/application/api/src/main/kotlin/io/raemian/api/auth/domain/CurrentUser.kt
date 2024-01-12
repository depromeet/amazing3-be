package io.raemian.api.auth.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

data class CurrentUser(
    val id: Long,
    val email: String,

    private val authorities: List<String> = listOf("ROLE_USER"),

    // not use field
    private val password: String? = null,
) : UserDetails, OAuth2User {

    override fun getName(): String = email

    override fun getAttributes(): MutableMap<String, Any> = mutableMapOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        authorities
            .map { SimpleGrantedAuthority(it) }
            .toMutableList()

    override fun getPassword(): String? = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
