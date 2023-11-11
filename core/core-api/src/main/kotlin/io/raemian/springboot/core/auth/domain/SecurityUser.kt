package io.raemian.springboot.core.auth.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class SecurityUser(
    username: String,
    password: String,
    authority: String
) : User(username, password, arrayListOf(SimpleGrantedAuthority(authority))) {

}


class SecurityGrantedAuthority : GrantedAuthority {
    override fun getAuthority(): String {
        return "USER"
    }
}