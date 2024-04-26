package io.raemian.api.cheer.model

import io.raemian.storage.db.core.cheer.Cheering

data class CheeringCountResult(
    val count: Long,
) {
    companion object {
        fun from(cheering: Cheering?): CheeringCountResult {
            return if (cheering == null) {
                CheeringCountResult(0)
            } else {
                CheeringCountResult(cheering.count)
            }
        }
    }
}
