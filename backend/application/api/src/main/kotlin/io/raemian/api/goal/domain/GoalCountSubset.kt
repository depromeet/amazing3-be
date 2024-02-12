package io.raemian.api.goal.domain

import io.raemian.storage.db.core.model.GoalExploreQueryResult

data class GoalCountSubset(
    val reaction: Long,
    val comment: Long,
    val task: Long,
    val goal: Long
) {
    constructor(result: GoalExploreQueryResult) : this(
        reaction = 0,
        comment = 0,
        task = 0,
        goal = result.goalCount
    )
}
