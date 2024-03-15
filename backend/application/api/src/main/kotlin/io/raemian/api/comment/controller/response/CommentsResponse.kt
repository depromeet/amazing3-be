package io.raemian.api.comment.controller.response

import io.raemian.storage.db.core.comment.Comment
import io.raemian.storage.db.core.user.User
import java.time.LocalDateTime

data class CommentsResponse(
    val comments: List<CommentResponse>,
    val commentCount: Int,
    val isMyGoal: Boolean,
) {
    companion object {
        fun from(comments: List<Comment>, userId: Long, isMyGoal: Boolean): CommentsResponse {
            val comments = comments
                .map { CommentResponse.from(it, userId) }
                .sortedBy { it.writtenAt }
            return CommentsResponse(comments, comments.size, isMyGoal)
        }
    }

    data class CommentResponse(
        val id: Long,
        val content: String,
        val writtenAt: LocalDateTime,
        val commenter: Commenter,
        val isMyComment: Boolean,
    ) {
        companion object {
            fun from(comment: Comment, userId: Long) = CommentResponse(
                id = comment.id!!,
                content = comment.content,
                writtenAt = comment.createdAt ?: LocalDateTime.now(),
                commenter = Commenter.from(comment.commenter),
                isMyComment = comment.commenter.id == userId,
            )
        }
    }

    data class Commenter(
        val username: String,
        val nickname: String,
        val image: String,
    ) {
        companion object {
            fun from(user: User) = Commenter(
                username = user.username!!,
                nickname = user.nickname!!,
                image = user.image,
            )
        }
    }
}
