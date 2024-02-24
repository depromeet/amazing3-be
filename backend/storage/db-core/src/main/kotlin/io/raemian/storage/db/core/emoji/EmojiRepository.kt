package io.raemian.storage.db.core.emoji

import org.springframework.data.jpa.repository.JpaRepository

interface EmojiRepository : JpaRepository<Emoji, Long> {

    override fun getById(id: Long): Emoji =
        findById(id).orElseThrow { NoSuchElementException("존재하지 않는 눌린 이모지입니다. $id") }
}
