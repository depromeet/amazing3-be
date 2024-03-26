package io.raemian.storage.db.core.common.pagination

import kotlin.math.min

object CursorPaginationTemplate {
    fun <CursorType, T : CursorExtractable<CursorType>> execute(
        id: Long,
        cursorId: CursorType,
        size: Int,
        query: TriFunction<Long, CursorType, Int, List<T>>,
    ): CursorPaginationResult<CursorType, T> {
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
