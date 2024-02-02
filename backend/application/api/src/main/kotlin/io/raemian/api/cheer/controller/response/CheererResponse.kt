package io.raemian.api.cheer.controller.response

import io.raemian.storage.db.core.cheer.Cheerer
import java.time.LocalDateTime

data class CheererResponse(
    val userId: Long,
    val userName: String,
    val userNickName: String,
    val userImageUrl: String,
    val cheeringAt: LocalDateTime?,
) {
    companion object {
        fun from(cheerer: Cheerer): CheererResponse {
            return if (cheerer.user == null) {
                CheererResponse(-1, "", "", "", cheerer.cheeringAt)
            } else {
                CheererResponse(cheerer.user!!.id!!, cheerer.user!!.username!!, cheerer.user!!.nickname!!, cheerer.user!!.image, cheerer.cheeringAt)
            }
        }
    }
}
