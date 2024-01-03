package io.raemian.api.goal

import io.raemian.api.goal.controller.response.GoalResponse
import io.raemian.api.goal.controller.response.GoalsResponse
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalReadService(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun findAllByUserId(userId: Long): GoalsResponse {
        val goals = goalRepository.findAllByUserId(userId)
        val sortedGoals = sortByDeadlineAscendingAndCreatedAtDescending(goals)
        return GoalsResponse.from(sortedGoals)
    }

    @Transactional(readOnly = true)
    fun findAllByUserName(userName: String): GoalsResponse {
        val user = userRepository.findByUserName(userName)
            .orElseThrow() { NoSuchElementException("존재하지 않는 유저입니다. $userName") }

        validateUserGoalsPublic(user)
        val goals = goalRepository.findAllByUserId(user.id!!)
        val sortedGoals = sortByDeadlineAscendingAndCreatedAtDescending(goals)
        return GoalsResponse.from(sortedGoals)
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): GoalResponse {
        val goal = goalRepository.getById(id)
        validateUserGoalsPublic(goal.user)
        return GoalResponse(goal)
    }

    private fun validateUserGoalsPublic(user: User) {
        if (!user.isGoalsPublic) {
            throw SecurityException()
        }
    }

    private fun sortByDeadlineAscendingAndCreatedAtDescending(goals: List<Goal>): List<Goal> {
        return goals.sortedWith(
            compareBy<Goal> { it.deadline }
                .thenByDescending { it.createdAt },
        )
    }
}
