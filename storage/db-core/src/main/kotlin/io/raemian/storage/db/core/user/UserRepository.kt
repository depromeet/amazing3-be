package io.raemian.storage.db.core.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    fun findByUserName(userName: String): Optional<User>

    override fun getById(id: Long): User =
        findById(id).orElseThrow { NoSuchElementException("존재하지 않는 유저입니다. $id") }
}
