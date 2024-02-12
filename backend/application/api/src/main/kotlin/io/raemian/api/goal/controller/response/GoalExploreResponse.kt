package io.raemian.api.goal.controller.response

import io.raemian.api.goal.domain.GoalExploreDTO

data class GoalExploreResponse(
    val goals: List<GoalExploreDTO>,
    val cursor: GoalExploreCursor
) {

    constructor(goals: List<GoalExploreDTO>): this(
        goals = goals,
        cursor = GoalExploreCursor(goals.lastOrNull()?.goal?.id ?: Long.MIN_VALUE)
    )
    data class GoalExploreCursor(
        val next: Long
    )
}
