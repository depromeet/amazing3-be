package io.raemian.api.emoji.controller.response

import io.raemian.storage.db.core.emoji.ReactedEmoji
import io.raemian.storage.db.core.user.User

data class ReactedEmojisResponse(
    val totalReactedEmojisCount: Int,
    val latestReactUserNickname: String?,
    val reactedEmojis: List<ReactedEmojiAndReactUsers>,
) {
    companion object {
        fun of(reactedEmojis: List<ReactedEmoji>, username: String): ReactedEmojisResponse {
            val reactedEmojiAndReactUsers = convert(reactedEmojis, username)

            val totalEmojisCount = reactedEmojiAndReactUsers.sumOf { it.reactCount }
            val latestReactUserNickname = reactedEmojis.lastOrNull()?.reactUser?.nickname
            return ReactedEmojisResponse(
                totalReactedEmojisCount = totalEmojisCount,
                latestReactUserNickname = latestReactUserNickname,
                reactedEmojis = reactedEmojiAndReactUsers,
            )
        }

        private fun convert(
            reactedEmojis: List<ReactedEmoji>,
            username: String,
        ): List<ReactedEmojiAndReactUsers> =
            reactedEmojis
                .filter { it.emoji.id != null }
                .groupBy { it.emoji.id }
                .mapValues { entry -> ReactedEmojiAndReactUsers.of(entry.value, username) }
                .values
                .toList()
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
            fun of(reactedEmojis: List<ReactedEmoji>, username: String): ReactedEmojiAndReactUsers {
                val emoji = reactedEmojis.first().emoji
                val reactUsers = getReactUsers(reactedEmojis)
                val isMyReaction = reactUsers.any { it.username == username }

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
                    .map(ReactUser::from)
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
