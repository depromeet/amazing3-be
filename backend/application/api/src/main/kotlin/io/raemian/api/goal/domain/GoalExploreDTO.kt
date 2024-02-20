package io.raemian.api.goal.domain

import io.raemian.api.user.domain.UserSubset
import io.raemian.storage.db.core.model.GoalExploreQueryResult

data class GoalExploreDTO(
    val user: UserSubset,
    val goal: GoalSubset,
    val count: GoalCountSubset,
) {
    constructor(result: GoalExploreQueryResult) : this(
        goal = GoalSubset(result),
        user = UserSubset(result),
        count = GoalCountSubset(result),
    )
}
