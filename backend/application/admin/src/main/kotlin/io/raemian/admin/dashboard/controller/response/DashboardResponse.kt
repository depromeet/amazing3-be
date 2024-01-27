package io.raemian.admin.dashboard.controller.response

import io.raemian.admin.dashboard.dto.ActiveUserStatics
import io.raemian.admin.dashboard.dto.GoalStatics
import io.raemian.admin.dashboard.dto.TaskStatics
import io.raemian.admin.dashboard.dto.UserStatics

data class DashboardResponse(
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
        ): DashboardResponse {
            return DashboardResponse(userStatics, goalStatics, taskStatics, activeUserStatics)
        }
    }
}
