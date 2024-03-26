package io.raemian.storage.db.core.common.pagination

import java.time.LocalDateTime
import kotlin.math.min

object CursorPaginationTemplate {
    fun <T : CursorExtractable> execute(
        id: Long,
        cursorId: LocalDateTime,
        size: Int,
        query: TriFunction<Long, LocalDateTime, Int, List<T>>,
    ): CursorPaginationResult<T> {
        val data = query.apply(id, cursorId, size + 1)
        val isLast = data.size != size + 1
        val nextCursor = if (isLast) null else data[size].cursorId()

        return CursorPaginationResult(
            data.subList(0, min(data.size, size)),
            nextCursor,
            isLast,
        )
    }

    fun <T : CursorExtractable> execute(
        id: Long,
        cursorId: Long,
        size: Int,
        query: TriFunction<Long, Long, Int, List<T>>,
    ): CursorPaginationResult<T> {
        val data = query.apply(id, cursorId, size + 1)
        val isLast = data.size != size + 1
        val nextCursor = if (isLast) null else data[size].cursorId()

        return CursorPaginationResult(
            data.subList(0, min(data.size, size)),
            nextCursor,
            isLast,
        )
    }
}
