package io.raemian.api.goal.model

import io.raemian.storage.db.core.cheer.GoalExploreQueryResult

data class GoalExploreCountSubset(
    val reaction: Long,
    val comment: Long,
    val task: Long,
    val goal: Long,
) {
    constructor(result: GoalExploreQueryResult) : this(
        reaction = 0,
        comment = result.commentCount,
        task = 0,
        goal = result.goalCount,
    )
}
