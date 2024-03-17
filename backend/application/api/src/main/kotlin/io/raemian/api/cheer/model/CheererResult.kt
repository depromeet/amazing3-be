package io.raemian.api.cheer.model

import io.raemian.storage.db.core.cheer.Cheerer
import java.time.LocalDateTime

data class CheererResult(
    val userId: Long,
    val userName: String,
    val userNickName: String,
    val userImageUrl: String,
    val cheeringAt: LocalDateTime?,
) {
    companion object {
        fun from(cheerer: Cheerer): CheererResult {
            return if (cheerer.user == null) {
                CheererResult(-1, "", "", "", cheerer.cheeringAt)
            } else {
                CheererResult(cheerer.user!!.id!!, cheerer.user!!.username!!, cheerer.user!!.nickname!!, cheerer.user!!.image, cheerer.cheeringAt)
            }
        }
    }
}
