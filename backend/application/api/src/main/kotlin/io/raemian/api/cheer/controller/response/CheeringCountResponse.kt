package io.raemian.api.cheer.controller.response

import io.raemian.storage.db.core.cheer.Cheering

data class CheeringCountResponse(
    val count: Long,
) {
    companion object {
        fun from(cheering: Cheering?): CheeringCountResponse {
            return if (cheering == null) {
                CheeringCountResponse(0)
            } else {
                CheeringCountResponse(cheering.count)
            }
        }
    }
}
