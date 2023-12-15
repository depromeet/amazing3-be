package io.raemian.storage.db.core.sticker

import org.springframework.data.jpa.repository.JpaRepository

interface StickerRepository : JpaRepository<Sticker, Long>
