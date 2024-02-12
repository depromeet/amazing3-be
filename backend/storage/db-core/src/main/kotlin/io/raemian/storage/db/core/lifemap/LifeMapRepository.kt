package io.raemian.storage.db.core.lifemap

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LifeMapRepository : JpaRepository<LifeMap, Long> {

    fun findFirstByUserId(userId: Long): LifeMap?

    fun findFirstByUserUsername(username: String): LifeMap?

    fun findAllByIdInOrderByIdDesc(ids: List<Long>): List<LifeMap>

    @Query(
        """
        SELECT
            map.id
        FROM
            LifeMap as map, 
            LifeMapCount as count 
        WHERE 1 = 1
            AND map.id = count.lifeMapId
            AND count.goalCount > 2
            AND map.id < :cursor
            AND map.isPublic = true
        ORDER BY 
            map.id DESC 
        LIMIT 5
    """,
    )
    fun explore(@Param("cursor") lifeMapId: Long): List<Long>
}
