package io.raemian.storage.db.core.goal

import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.model.GoalExploreQueryResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface GoalRepository : JpaRepository<Goal, Long> {
    fun findUserByCreatedAtGreaterThanEqual(createdAt: LocalDateTime): List<Goal>

    fun countGoalByLifeMap(lifeMap: LifeMap): Int

    override fun getById(id: Long): Goal =
        findById(id).orElseThrow() { NoSuchElementException("목표가 없습니다 $id") }

    @Query(
        """
        SELECT 
            new io.raemian.storage.db.core.model.GoalExploreQueryResult(
                goal.id,
                goal.title,
                goal.description,
                goal.deadline,
                goal.sticker.url,
                goal.tag.content,
                goal.createdAt,
                map.id,
                count.goalCount,
                count.historyCount,
                count.viewCount,
                user.id,
                user.nickname,
                user.username,
                user.image
            )
        FROM 
            Goal as goal,
            LifeMap as map,
            USERS  as user,
            LifeMapCount as count
        WHERE 1 = 1
            AND goal.lifeMap.id = map.id
            AND map.user.id = user.id
            AND map.id = count.lifeMapId
            AND map.isPublic = true
            AND goal.id < :cursor
        ORDER BY 
            goal.id DESC
        LIMIT 10
    """,
    )
    fun explore(@Param("cursor") goalId: Long): List<GoalExploreQueryResult>
}
