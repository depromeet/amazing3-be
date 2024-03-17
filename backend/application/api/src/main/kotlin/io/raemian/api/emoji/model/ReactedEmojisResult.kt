package io.raemian.api.emoji.model

import io.raemian.storage.db.core.emoji.ReactedEmoji
import io.raemian.storage.db.core.user.User

data class ReactedEmojisResult(
    val totalReactedEmojisCount: Int,
    val totalReactUserCount: Int,
    val latestReactUserNickname: String?,
    val reactedEmojis: List<ReactedEmojiAndReactUsers>,
) {
    companion object {
        fun of(reactedEmojis: List<ReactedEmoji>, userId: Long): ReactedEmojisResult {
            val reactedEmojiAndReactUsers = convert(reactedEmojis, userId)

            val reactedUserCount = countTotalReactUser(reactedEmojiAndReactUsers)
            val totalEmojisCount = reactedEmojiAndReactUsers.sumOf { it.reactCount }

            val latestReactUserNickname = reactedEmojis.lastOrNull()?.reactUser?.nickname
            return ReactedEmojisResult(
                totalReactedEmojisCount = totalEmojisCount,
                totalReactUserCount = reactedUserCount,
                latestReactUserNickname = latestReactUserNickname,
                reactedEmojis = reactedEmojiAndReactUsers,
            )
        }

        private fun convert(
            reactedEmojis: List<ReactedEmoji>,
            userId: Long,
        ): List<ReactedEmojiAndReactUsers> =
            reactedEmojis
                .filter { it.emoji.id != null }
                .groupBy { it.emoji.id }
                .mapValues { entry -> ReactedEmojiAndReactUsers.of(entry.value, userId) }
                .values
                .toList()

        private fun countTotalReactUser(reactedEmojiAndReactUsers: List<ReactedEmojiAndReactUsers>) =
            reactedEmojiAndReactUsers
                .flatMap { it.reactUsers }
                .map { it.username }
                .distinct()
                .size
    }

    data class ReactedEmojiAndReactUsers(
        val id: Long?,
        val name: String,
        val url: String,
        val reactCount: Int,
        val isMyReaction: Boolean,
        val reactUsers: List<ReactUser>,
    ) {
        companion object {
            fun of(reactedEmojis: List<ReactedEmoji>, userId: Long): ReactedEmojiAndReactUsers {
                val emoji = reactedEmojis.first().emoji
                val reactUsers = getReactUsers(reactedEmojis)
                val isMyReaction = reactedEmojis.any { userId == it.reactUser.id }

                return ReactedEmojiAndReactUsers(
                    id = emoji.id,
                    name = emoji.name,
                    url = emoji.url,
                    reactCount = reactUsers.size,
                    isMyReaction = isMyReaction,
                    reactUsers = reactUsers,
                )
            }

            private fun getReactUsers(reactedEmojis: List<ReactedEmoji>) =
                reactedEmojis
                    .map { it.reactUser }
                    .filter { it.username != null }
                    .filter { it.nickname != null }
                    .map(ReactUser.Companion::from)
        }
    }

    data class ReactUser(
        val username: String,
        val nickname: String,
        val image: String,
    ) {
        companion object {
            fun from(user: User) = ReactUser(
                username = user.username!!,
                nickname = user.nickname!!,
                image = user.image,
            )
        }
    }
}
