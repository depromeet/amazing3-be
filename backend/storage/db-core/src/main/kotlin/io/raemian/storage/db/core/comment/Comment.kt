package io.raemian.storage.db.core.comment

import io.raemian.storage.db.core.common.BaseEntity
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Nationalized
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "COMMENTS")
class Comment(
    @ManyToOne
    @JoinColumn(name = "goal_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val goal: Goal,

    @ManyToOne
    @JoinColumn(name = "commenter_id")
    val commenter: User,

    @Column(nullable = false)
    @Nationalized
    var content: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {
    companion object {
        private const val CONTENT_CHARACTER_LIMIT = 50
    }

    init {
        require(content.length <= CONTENT_CHARACTER_LIMIT) {
            "글자수 제한${CONTENT_CHARACTER_LIMIT}자를 초과했습니다."
        }
    }
}
