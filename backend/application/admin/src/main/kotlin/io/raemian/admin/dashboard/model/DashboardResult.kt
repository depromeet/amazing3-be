package io.raemian.admin.dashboard.model

data class DashboardResult(
    val userStatics: UserStatics,
    val goalStatics: GoalStatics,
    val taskStatics: TaskStatics,
    val activeUserStatics: ActiveUserStatics,
) {
    companion object {
        fun from(
            userStatics: UserStatics,
            goalStatics: GoalStatics,
            taskStatics: TaskStatics,
            activeUserStatics: ActiveUserStatics,
        ): DashboardResult {
            return DashboardResult(userStatics, goalStatics, taskStatics, activeUserStatics)
        }
    }
}
