package io.raemian.api.support.extension

import java.time.LocalDate

fun LocalDate.format(): String {
    var month = (this.monthValue).toString()
    if (month.length == 1) {
        month = "0$month"
    }

    return "${this.year}.$month"
}
