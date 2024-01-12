package io.raemian.storage.db.core.sticker

import org.springframework.data.jpa.repository.JpaRepository

interface StickerRepository : JpaRepository<Sticker, Long> {

    override fun getById(id: Long): Sticker =
        findById(id).orElseThrow { NoSuchElementException("존재하지 않는 스티커입니다. $id") }
}
