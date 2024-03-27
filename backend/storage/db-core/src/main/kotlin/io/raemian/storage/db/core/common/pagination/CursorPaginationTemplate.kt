package io.raemian.storage.db.core.common.pagination

import kotlin.math.min

object CursorPaginationTemplate {
    fun <T : CursorExtractable> execute(
        id: Long,
        cursorId: Long,
        size: Int,
        query: TriFunction<Long, Long, Int, List<T>>,
    ): PaginationResult<T> {
        val data = query.apply(id, cursorId, size + 1)
        val isLast = data.size != size + 1
        val nextCursor = if (isLast) null else data[size].cursorId()

        return PaginationResult(
            data.subList(0, min(data.size, size)),
            nextCursor,
            isLast,
        )
    }
}
