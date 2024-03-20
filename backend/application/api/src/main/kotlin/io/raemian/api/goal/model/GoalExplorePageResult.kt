package io.raemian.api.goal.model

data class GoalExplorePageResult(
    val goals: List<GoalExploreResult>,
    val cursor: GoalExploreCursor,
) {

    constructor(goals: List<GoalExploreResult>) : this(
        goals = goals,
        cursor = GoalExploreCursor(goals.lastOrNull()?.goal?.id ?: Long.MIN_VALUE),
    )
    data class GoalExploreCursor(
        val next: Long,
    )
}
