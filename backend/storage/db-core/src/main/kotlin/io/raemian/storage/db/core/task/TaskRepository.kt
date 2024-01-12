package io.raemian.storage.db.core.task

import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long> {

    override fun getById(id: Long): Task =
        findById(id).orElseThrow() { NoSuchElementException("Task가 없습니다 $id") }
}
