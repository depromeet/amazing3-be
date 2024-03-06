package io.raemian.storage.db.core.comment

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    override fun getById(id: Long): Comment =
        findById(id).orElseThrow() { NoSuchElementException("Comment가 없습니다 $id") }
}
