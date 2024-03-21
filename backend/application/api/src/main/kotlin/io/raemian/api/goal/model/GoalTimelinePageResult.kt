package io.raemian.api.goal.model

import io.raemian.api.emoji.model.EmojiCountSubset
import io.raemian.api.emoji.model.ReactedEmojisResult
import io.raemian.storage.db.core.goal.model.GoalQueryResult

data class GoalTimelinePageResult(
    val goal: GoalQueryResult,
    val count: GoalTimelineCountSubset?,
    val emojis: List<EmojiCountSubset>,
) {
    companion object {
        fun from(goal: GoalQueryResult, count: GoalTimelineCountSubset?, reactedEmojisResult: ReactedEmojisResult?): GoalTimelinePageResult {
            if (reactedEmojisResult == null) {
                return GoalTimelinePageResult(
                    goal,
                    count,
                    listOf(),
                )
            }

            return GoalTimelinePageResult(
                goal,
                count,
                reactedEmojisResult.reactedEmojis.map {
                    EmojiCountSubset(
                        id = it.id ?: -1,
                        name = it.name,
                        url = it.url,
                        reactCount = it.reactCount,
                        isMyReaction = it.isMyReaction,
                    )
                },
            )
        }
    }
}
