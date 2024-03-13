package io.raemian.api.goal.domain

import io.raemian.api.emoji.controller.response.ReactedEmojisResponse
import io.raemian.api.emoji.domain.EmojiCountSubset
import io.raemian.api.user.domain.UserSubset
import io.raemian.storage.db.core.emoji.EmojiCount
import io.raemian.storage.db.core.model.GoalExploreQueryResult

data class GoalExploreDTO(
    val user: UserSubset,
    val goal: GoalSubset,
    val count: GoalCountSubset,
    val emojis: List<EmojiCountSubset>
) {
    companion object {
        fun from(explore: GoalExploreQueryResult, reactedEmojisResponse: ReactedEmojisResponse?): GoalExploreDTO {
            val emojis = reactedEmojisResponse?.reactedEmojis?.map {
                EmojiCountSubset(
                    id = it.id ?: -1,
                    url = it.url,
                    name = it.name,
                    count = it.reactCount.toLong(),
                    isMine = it.isMyReaction
                )
            } ?: listOf()
            return GoalExploreDTO(
                goal = GoalSubset(explore),
                user = UserSubset(explore),
                count = GoalCountSubset(explore),
                emojis = emojis
            )
        }
    }
}
