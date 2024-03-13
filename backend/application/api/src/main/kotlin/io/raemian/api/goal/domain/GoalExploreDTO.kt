package io.raemian.api.goal.domain

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
        fun from(explore: GoalExploreQueryResult, emojiCounts: List<EmojiCount>): GoalExploreDTO {

            return GoalExploreDTO(
                goal = GoalSubset(explore),
                user = UserSubset(explore),
                count = GoalCountSubset(explore),
                emojis = emojiCounts.map { EmojiCountSubset(it) }
            )
        }
    }
}
