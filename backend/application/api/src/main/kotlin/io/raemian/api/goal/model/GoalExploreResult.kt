package io.raemian.api.goal.model

import io.raemian.api.emoji.model.EmojiCountSubset
import io.raemian.api.emoji.model.ReactedEmojisResult
import io.raemian.api.user.model.UserSubset
import io.raemian.storage.db.core.cheer.GoalExploreQueryResult

data class GoalExploreResult(
    val user: UserSubset,
    val goal: GoalSubset,
    val count: GoalCountSubset,
    val emojis: List<EmojiCountSubset>,
) {
    companion object {
        fun from(explore: GoalExploreQueryResult, reactedEmojisResponse: ReactedEmojisResult?): GoalExploreResult {
            val emojis = reactedEmojisResponse?.reactedEmojis?.map {
                EmojiCountSubset(
                    id = it.id ?: -1,
                    url = it.url,
                    name = it.name,
                    reactCount = it.reactCount.toLong(),
                    isMyReaction = it.isMyReaction,
                )
            } ?: listOf()
            return GoalExploreResult(
                goal = GoalSubset(explore),
                user = UserSubset(explore),
                count = GoalCountSubset(explore),
                emojis = emojis,
            )
        }
    }
}
