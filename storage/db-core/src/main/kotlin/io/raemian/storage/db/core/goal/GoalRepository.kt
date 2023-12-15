package io.raemian.storage.db.core.goal

import org.springframework.data.jpa.repository.JpaRepository

interface GoalRepository : JpaRepository<Goal, Long> {
    fun findAllByUserId(userId: Long): List<Goal>

    override fun getById(id: Long): Goal =
        findById(id)
            .orElseThrow() { NoSuchElementException("목표가 없습니다 $id") }
}
