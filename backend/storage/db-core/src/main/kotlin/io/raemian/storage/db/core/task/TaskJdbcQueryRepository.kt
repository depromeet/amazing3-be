package io.raemian.storage.db.core.task

import io.raemian.storage.db.core.task.model.GoalTaskCountQueryResult
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class TaskJdbcQueryRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun findAllGoalTaskCountByGoalIdIn(goalIds: List<Long>): List<GoalTaskCountQueryResult> {
        val sql =
            """
           SELECT
               t.GOAL_ID AS GOAL_ID,
               COUNT(t.GOAL_ID) AS TASK_COUNT
           FROM tasks t
           WHERE 1 = 1
               AND t.GOAL_ID IN (:goalIds)
           GROUP BY t.GOAL_ID
            """.trimIndent()

        val namedParameter = MapSqlParameterSource()
            .addValue("goalIds", goalIds)

        return jdbcTemplate.query(sql, namedParameter) {
                rs, rowNum ->
            GoalTaskCountQueryResult(
                goalId = rs.getLong("GOAL_ID"),
                taskCounts = rs.getInt("TASK_COUNT"),
            )
        }
    }
}
