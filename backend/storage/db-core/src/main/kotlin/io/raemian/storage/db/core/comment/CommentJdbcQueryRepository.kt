package io.raemian.storage.db.core.comment

import io.raemian.storage.db.core.comment.model.GoalCommentCountQueryResult
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CommentJdbcQueryRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun findAllGoalCommentCountByGoalIdIn(goalIds: List<Long>): List<GoalCommentCountQueryResult> {
        val sql =
            """
           SELECT
               c.GOAL_ID AS GOAL_ID,
               COUNT(GOAL_ID) AS COMMENT_COUNT
           FROM comment_counts c
           WHERE 1 = 1
               AND c.GOAL_ID IN (:goalIds)
           GROUP BY c.GOAL_ID
            """.trimIndent()

        val namedParameter = MapSqlParameterSource()
            .addValue("goalIds", goalIds)

        return jdbcTemplate.query(sql, namedParameter) {
                rs, rowNum ->
            GoalCommentCountQueryResult(
                goalId = rs.getLong("GOAL_ID"),
                commentCounts = rs.getInt("COMMENT_COUNT"),
            )
        }
    }
}
