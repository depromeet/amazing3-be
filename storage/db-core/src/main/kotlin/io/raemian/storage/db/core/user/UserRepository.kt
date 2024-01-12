package io.raemian.storage.db.core.user

import io.raemian.storage.db.core.user.enums.OAuthProvider
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmailAndProvider(email: String, provider: OAuthProvider): User?

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    override fun getById(id: Long): User =
        findById(id).orElseThrow { NoSuchElementException("존재하지 않는 유저입니다. $id") }
}
