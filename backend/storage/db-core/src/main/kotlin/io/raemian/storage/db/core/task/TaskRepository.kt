package io.raemian.storage.db.core.task

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface TaskRepository : JpaRepository<Task, Long> {

    fun findUserByCreatedAtGreaterThanEqual(createdAt: LocalDateTime): List<Task>

    override fun getById(id: Long): Task =
        findById(id).orElseThrow() { NoSuchElementException("Task가 없습니다 $id") }
}
