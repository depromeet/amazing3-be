package io.raemian.storage.db.core.comment

import org.springframework.data.jpa.repository.JpaRepository

interface GoalCommentReadTimeRepository : JpaRepository<GoalCommentReadTime, Long> {

    fun findByGoalId(goalId: Long): GoalCommentReadTime?
}
