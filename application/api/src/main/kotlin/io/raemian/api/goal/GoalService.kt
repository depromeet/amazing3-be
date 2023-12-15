package io.raemian.api.goal

import io.raemian.api.goal.controller.response.GoalResponse
import io.raemian.api.goal.controller.response.GoalsResponse
import io.raemian.api.support.SecurityUtil
import io.raemian.storage.db.core.goal.GoalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalService(
    private val goalRepository: GoalRepository,
) {

    @Transactional(readOnly = true)
    fun findAllByUserId(): GoalsResponse {
        val currentUserId = SecurityUtil.currentUserId()
        val goals = goalRepository.findAllByUserId(currentUserId)
        return GoalsResponse(goals)
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): GoalResponse {
        val goal = goalRepository.getById(id)
        return GoalResponse(goal)
    }
}
