package io.raemian.api.goal

import io.raemian.api.goal.controller.response.GoalResponse
import io.raemian.api.goal.controller.response.GoalsResponse
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalReadService(
    private val goalRepository: GoalRepository,
) {
    @Transactional(readOnly = true)
    fun findAllByUserId(userId: Long): GoalsResponse {
        val goals = goalRepository.findAllByUserId(userId)
        val sortedGoals = sortByDeadlineAscendingAndCreatedAtDescending(goals)
        return GoalsResponse.from(sortedGoals)
    }

    @Transactional(readOnly = true)
    fun findAllByUserName(username: String): GoalsResponse {
        val goals = goalRepository.findAllByUserUserName(username)
        val sortedGoals = sortByDeadlineAscendingAndCreatedAtDescending(goals)
        return GoalsResponse.from(sortedGoals)
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): GoalResponse {
        val goal = goalRepository.getById(id)
        return GoalResponse(goal)
    }

    private fun sortByDeadlineAscendingAndCreatedAtDescending(goals: List<Goal>): List<Goal> {
        return goals.sortedWith(
            compareBy<Goal> { it.deadline }
                .thenByDescending { it.createdAt },
        )
    }
}
