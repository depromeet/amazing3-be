package io.raemian.api.goal.model

data class GoalTimelineCountSubset(
    val comment: Int,
    val task: Int,
) {
    companion object {
        fun of(commentCount: Int, taskCount: Int): GoalTimelineCountSubset {
            return GoalTimelineCountSubset(commentCount, taskCount)
        }
    }
}
