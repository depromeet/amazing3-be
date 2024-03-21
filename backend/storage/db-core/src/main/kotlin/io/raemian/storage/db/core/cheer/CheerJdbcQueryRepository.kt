package io.raemian.storage.db.core.cheer

import io.raemian.storage.db.core.cheer.model.CheererQueryResult
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class CheerJdbcQueryRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun findAllCheererWithCursor(lifeMapId: Long, cursor: Long, size: Int): List<CheererQueryResult> {
        val sql =
            """
           SELECT
               c.ID AS CHEERER_ID,
               u.ID AS USER_ID,
               u.USERNAME AS USERNAME,
               u.NICKNAME AS NICKNAME,
               u.IMAGE AS IMAGE_URL,
               c.CHEERING_AT AS CHEERING_AT
           FROM cheerer c
           LEFT OUTER JOIN users u ON c.USER_ID = u.ID
           WHERE 1 = 1
               AND c.LIFE_MAP_ID = :lifeMapId
               AND c.ID <= :cursor
           ORDER BY c.ID DESC
           LIMIT :size
            """.trimIndent()

        val namedParameter = MapSqlParameterSource()
            .addValue("lifeMapId", lifeMapId)
            .addValue("cursor", cursor)
            .addValue("size", size)

        return jdbcTemplate.query(sql, namedParameter) {
                rs, rowNum ->
            CheererQueryResult(
                cheererId = rs.getLong("CHEERER_ID"),
                userId = rs.getLong("USER_ID"),
                userName = rs.getString("USERNAME"),
                userNickName = rs.getString("NICKNAME"),
                userImageUrl = rs.getString("IMAGE_URL"),
                cheeringAt = rs.getObject("CHEERING_AT", LocalDateTime::class.java),
            )
        }
    }
}
