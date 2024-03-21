package io.raemian.storage.db.core.comment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentCountRepository : JpaRepository<CommentCount, Long> {
    fun findByGoalId(goalId: Long): CommentCount?
}