package io.raemian.storage.db.core.goalsandpublication

import io.raemian.storage.db.core.goal.Goal
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class GoalsAndPublicationJpaRepository(
    private val entityManager: EntityManager,
) : GoalsAndPublicationRepository {

    companion object {
        const val USER_NAME = "userName"

        const val GET_GOALS_AND_PUBLICATION_QUERY = """
            SELECT U.isGoalsPublic, G, U.id
            FROM USERS as U
            LEFT JOIN Goal as G
            ON U.id = G.user.id
            WHERE U.userName = :$USER_NAME
        """
    }

    override fun findGoalsAndPublicationByUserName(userName: String): GoalsAndPublication {
        val queryResult = getQueryResult(userName)
        val isGoalsPublic = extractGoalsPublic(queryResult)
        val goals = extractGoals(queryResult)

        return GoalsAndPublication(goals, isGoalsPublic)
    }

    private fun getQueryResult(userName: String): QueryResult {
        val query = entityManager
            .createQuery(GET_GOALS_AND_PUBLICATION_QUERY, Array<Any>::class.java)
        query.setParameter(USER_NAME, userName)
        return QueryResult(query.resultList)
    }

    private fun extractGoalsPublic(queryResult: QueryResult): Boolean =
        queryResult.result
            .stream()
            .findAny()
            .get()[0] as Boolean

    private fun extractGoals(queryResult: QueryResult): List<Goal> =
        queryResult.result
            .stream()
            .filter { it[1] != null }
            .map { it[1] as Goal }
            .toList()
}

data class QueryResult(
    val result: MutableList<Array<Any>>,
)
