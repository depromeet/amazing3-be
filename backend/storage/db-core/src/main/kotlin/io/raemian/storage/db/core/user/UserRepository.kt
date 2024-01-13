package io.raemian.storage.db.core.user

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    fun findUserByCreatedAtGreaterThanEqual(createdAt: LocalDateTime): List<User>

    override fun getById(id: Long): User =
        findById(id).orElseThrow { NoSuchElementException("존재하지 않는 유저입니다. $id") }
}
