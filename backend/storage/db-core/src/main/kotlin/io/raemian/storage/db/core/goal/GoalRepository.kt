package io.raemian.storage.db.core.goal

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface GoalRepository : JpaRepository<Goal, Long> {

    fun findUserByCreatedAtGreaterThanEqual(createdAt: LocalDateTime): List<Goal>

    override fun getById(id: Long): Goal =
        findById(id).orElseThrow() { NoSuchElementException("목표가 없습니다 $id") }
}
