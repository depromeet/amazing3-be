package io.raemian.api.goal.model

import io.raemian.api.emoji.model.EmojiCountSubset
import io.raemian.api.emoji.model.ReactedEmojisResult
import io.raemian.storage.db.core.goal.model.GoalQueryResult

data class GoalTimelinePageResult(
    val goal: GoalQueryResult,
    val counts: GoalTimelineCountSubset?,
    val emojis: List<EmojiCountSubset>,
) {
    companion object {
        fun from(goal: GoalQueryResult, counts: GoalTimelineCountSubset?, reactedEmojisResult: ReactedEmojisResult?): GoalTimelinePageResult {
            if (reactedEmojisResult == null) {
                return GoalTimelinePageResult(
                    goal = goal,
                    counts = counts,
                    emojis = emptyList(),
                )
            }

            return GoalTimelinePageResult(
                goal = goal,
                counts = counts,
                emojis = reactedEmojisResult.reactedEmojis.map {
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
