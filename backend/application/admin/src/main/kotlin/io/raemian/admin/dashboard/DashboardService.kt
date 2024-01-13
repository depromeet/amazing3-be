package io.raemian.admin.dashboard

import io.raemian.admin.dashboard.controller.response.DashboardResponse
import io.raemian.admin.dashboard.dto.GoalStatics
import io.raemian.admin.dashboard.dto.TaskStatics
import io.raemian.admin.dashboard.dto.UserStatics
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.task.Task
import io.raemian.storage.db.core.task.TaskRepository
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class DashboardService(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository,
) {

    @Transactional(readOnly = true)
    fun getDashBoard(): DashboardResponse {
        return DashboardResponse.from(
            getUserStatics(),
            getGoalStatics(),
            getTaskStatics(),
        )
    }

    private fun getUserStatics(): UserStatics {
        val userCount: Long = userRepository.count()
        val todayCreatedUsers: List<User> =
            userRepository.findUserByCreatedAtGreaterThanEqual(LocalDate.now().atStartOfDay())

        return UserStatics(userCount, todayCreatedUsers.size)
    }

    private fun getGoalStatics(): GoalStatics {
        val goalCount: Long = goalRepository.count()
        val todayCreatedGoals: List<Goal> =
            goalRepository.findUserByCreatedAtGreaterThanEqual(LocalDate.now().atStartOfDay())

        return GoalStatics(goalCount, todayCreatedGoals.size)
    }

    private fun getTaskStatics(): TaskStatics {
        val taskCount: Long = taskRepository.count()
        val todayCreatedTasks: List<Task> =
            taskRepository.findUserByCreatedAtGreaterThanEqual(LocalDate.now().atStartOfDay())

        return TaskStatics(taskCount, todayCreatedTasks.size)
    }
}
