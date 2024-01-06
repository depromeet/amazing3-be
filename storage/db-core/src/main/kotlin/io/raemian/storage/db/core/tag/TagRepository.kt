package io.raemian.storage.db.core.tag

import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {

    override fun getById(id: Long): Tag =
        findById(id).orElseThrow { NoSuchElementException("존재하지 않는 태그입니다. $id") }

    fun existsTagsByContent(content: String): Boolean
}
