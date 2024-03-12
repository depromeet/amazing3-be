package io.raemian.storage.db.core.comment

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CommentRepository : JpaRepository<Comment, Long> {

    fun findAllByGoalId(goalId: Long): List<Comment>

    fun existsByCreatedAtGreaterThan(createdAt: LocalDateTime): Boolean

    override fun getById(id: Long): Comment =
        findById(id).orElseThrow() { NoSuchElementException("Comment가 없습니다 $id") }
}
