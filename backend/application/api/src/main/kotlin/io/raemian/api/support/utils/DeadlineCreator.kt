package io.raemian.api.support.utils

import java.time.LocalDate
import java.time.Month
import java.time.Year

object DeadlineCreator {

    private const val DAY_OF_MONTH = 1

    fun create(year: String, month: String): LocalDate {
        val parsedYear = parseYear(year)
        val parsedMonth = parseMonth(month)
        return LocalDate.of(parsedYear, parsedMonth, DAY_OF_MONTH)
    }

    private fun parseYear(year: String): Int {
        validateYearFormat(year)
        return year.toInt()
    }

    private fun validateYearFormat(year: String) {
        runCatching {
            Year.parse(year)
        }.onFailure {
            throw IllegalArgumentException()
        }
    }

    private fun parseMonth(month: String): Month {
        val result = runCatching {
            Month.of(month.toInt())
        }

        return result.getOrNull()
            ?: throw IllegalArgumentException()
    }
}
