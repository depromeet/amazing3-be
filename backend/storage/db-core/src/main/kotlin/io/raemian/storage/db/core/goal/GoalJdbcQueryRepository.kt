package io.raemian.storage.db.core.goal

import io.raemian.storage.db.core.goal.model.GoalQueryResult
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class GoalJdbcQueryRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun findAllByLifeMapWithOffset(lifeMapId: Long, page: Int, size: Int): List<GoalQueryResult> {
        val sql =
            """
           SELECT
               g.ID AS GOAL_ID,
               g.TITLE AS TITLE,
               g.DESCRIPTION AS DESCRIPTION,
               g.DEADLINE AS DEADLINE,
               s.URL AS STICKER_URL,
               t.CONTENT AS TAG,
               g.CREATED_AT AS CREATED_AT
           FROM goals g
           LEFT OUTER JOIN tags t ON g.TAG_ID = t.ID
           LEFT OUTER JOIN stickers s ON g.STICKER_ID = s.ID
           WHERE 1 = 1
               AND g.LIFE_MAP_ID = :lifeMapId
           ORDER BY g.DEADLINE DESC
           LIMIT :size
           OFFSET :page
            """.trimIndent()

        val namedParameter = MapSqlParameterSource()
            .addValue("lifeMapId", lifeMapId)
            .addValue("page", page)
            .addValue("size", size)

        return jdbcTemplate.query(sql, namedParameter) {
                rs, rowNum ->
            GoalQueryResult(
                goalId = rs.getLong("GOAL_ID"),
                title = rs.getString("TITLE"),
                description = rs.getString("DESCRIPTION"),
                deadline = rs.getObject("DEADLINE", LocalDateTime::class.java),
                stickerUrl = rs.getString("STICKER_URL"),
                tag = rs.getString("TAG"),
                createdAt = rs.getObject("CREATED_AT", LocalDateTime::class.java),
            )
        }
    }
}
