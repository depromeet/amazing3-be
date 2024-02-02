package io.raemian.admin.scheduler

import io.raemian.admin.dashboard.DashboardService
import io.raemian.infra.logging.logger.SlackLogger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class DashboardScheduler(
    private val slackLogger: SlackLogger,
    private val dashboardService: DashboardService,
) {

    @Scheduled(cron = "0 0 23 * * *")
    fun sendDashboard() {
        val dashboard = dashboardService.getDashBoard()

        slackLogger.sendDashboard(
            dashboard.userStatics.total.toString(),
            dashboard.userStatics.todayIncrease.toString(),
            dashboard.goalStatics.total.toString(),
            dashboard.goalStatics.todayIncrease.toString(),
            dashboard.taskStatics.total.toString(),
            dashboard.taskStatics.todayIncrease.toString(),
            dashboard.activeUserStatics.perTodayPercent.toString(),
        )
    }
}
