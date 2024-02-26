package io.raemian.api.emoji.controller.response

import io.raemian.storage.db.core.emoji.ReactedEmoji
import io.raemian.storage.db.core.user.User

data class ReactedEmojisResponse(
    val totalReactedEmojisCount: Int,
    val reactedEmojis: List<ReactedEmojiAndReactUsers>,
) {
    companion object {
        fun of(reactedEmojis: List<ReactedEmoji>): ReactedEmojisResponse {
            val reactedEmojiAndReactUsers = convert(reactedEmojis)
            val totalEmojisCount = reactedEmojiAndReactUsers.sumOf { it.count }
            return ReactedEmojisResponse(totalEmojisCount, reactedEmojiAndReactUsers)
        }

        private fun convert(reactedEmojis: List<ReactedEmoji>): List<ReactedEmojiAndReactUsers> =
            reactedEmojis
                .filter { it.emoji.id != null }
                .groupBy { it.emoji.id!! }
                .mapValues { entry -> ReactedEmojiAndReactUsers.of(entry.value) }
                .values
                .toList()
    }

    data class ReactedEmojiAndReactUsers(
        val id: Long?,
        val name: String,
        val url: String,
        val count: Int,
        val reactUsers: Set<ReactUser>,
    ) {
        companion object {
            fun of(reactedEmojis: List<ReactedEmoji>): ReactedEmojiAndReactUsers {
                val emoji = reactedEmojis.first().emoji
                val reactUsers = getReactUsers(reactedEmojis)

                return ReactedEmojiAndReactUsers(
                    id = emoji.id,
                    name = emoji.name,
                    url = emoji.url,
                    count = reactUsers.size,
                    reactUsers = reactUsers,
                )
            }

            private fun getReactUsers(reactedEmojis: List<ReactedEmoji>) =
                reactedEmojis.stream()
                    .map { it.reactUser }
                    .filter { it.username != null }
                    .filter { it.nickname != null }
                    .map(ReactUser::from)
                    .toList()
                    .toSet()
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
