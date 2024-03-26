package io.raemian.storage.db.core.common.pagination

interface CursorExtractable<CursorType> {
    fun cursorId(): CursorType
}
