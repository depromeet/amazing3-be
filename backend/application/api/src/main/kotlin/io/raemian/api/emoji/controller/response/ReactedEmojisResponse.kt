package io.raemian.api.emoji.controller.response

import io.raemian.storage.db.core.emoji.ReactedEmoji
import io.raemian.storage.db.core.user.User

data class ReactedEmojisResponse(
    val totalReactedEmojisCount: Int,
    val reactedEmojis: Map<Long, ReactedEmojiDTO>,
) {
    companion object {
        fun of(reactedEmojis: List<ReactedEmoji>): ReactedEmojisResponse {
            val mapValues = reactedEmojis
                .filter { it.emoji.id != null }
                .groupBy { it.emoji.id!! }
                .mapValues(ReactedEmojiDTO.Companion::from)

            val totalEmojisCount = mapValues.values.sumOf { it.count }
            return ReactedEmojisResponse(totalEmojisCount, mapValues)
        }
    }

    data class ReactedEmojiDTO(
        val id: Long?,
        val name: String,
        val url: String,
        val count: Int,
        val reactUsers: Set<ReactUserDTO>,
    ) {
        companion object {
            fun from(reactedEmojis2: Map.Entry<Long, List<ReactedEmoji>>): ReactedEmojiDTO {
                val reactedEmojis = reactedEmojis2.value
                val emoji = reactedEmojis.first().emoji
                val reactUsers = getReactUsers(reactedEmojis)

                return ReactedEmojiDTO(
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
                    .filter { it.nickname != null }
                    .map(ReactedEmojisResponse::ReactUserDTO)
                    .toList()
                    .toSet()
        }
    }

    data class ReactUserDTO(
        val id: Long,
        val nickname: String,
        val image: String,
    ) {
        constructor(user: User) : this(
            id = user.id!!,
            nickname = user.nickname!!,
            image = user.image,
        )
    }
}
