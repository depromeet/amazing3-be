package io.raemian.storage.db.core.goalsandpublication

import org.springframework.stereotype.Repository

@Repository
interface GoalsAndPublicationRepository {

    fun findGoalsAndPublicationByUserName(userName: String): GoalsAndPublication
}
