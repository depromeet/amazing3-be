package io.raemian.api.support

import io.raemian.api.support.utils.RaemianLocalDateUtil
import io.raemian.api.support.utils.format
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate

class RaemianLocalDateUtilTest {

    @Test
    @DisplayName("RaemianLocalDate를 통해 연도와 날짜 string으로 LocalDate를 만들 수 있다.")
    fun createRaemianLocalDateTest() {
        // given
        val year = "2023"
        val month = "11"

        // when
        val localDate = RaemianLocalDateUtil.of(year, month)

        // then
        assertThat(localDate.year.toString()).isEqualTo(year)
        assertThat(localDate.monthValue.toString()).isEqualTo(month)
    }

    @Test
    @DisplayName("RaemianLocalDate를 통해 생성된 LocalDate의 날짜가 모두 같다.")
    fun createRaemianLocalDateFixedDayTest() {
        // given
        val year1 = (1..2024).random().toString()
        val month1 = (1..12).random().toString()

        val year2 = (1..2024).random().toString()
        val month2 = (1..12).random().toString()

        // when
        val localDate1 = RaemianLocalDateUtil.of(year1, month1)
        val localDate2 = RaemianLocalDateUtil.of(year2, month2)

        // then
        assertThat(localDate1.dayOfMonth).isEqualTo(localDate2.dayOfMonth)
    }

    @DisplayName("RaemianLocalDate를 통해 생성된 LocalDate생성시 연도나 월의 형식이 잘못 되면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = ["13", "-1", "abcdabcd", "331er"])
    fun createRaemianLocalDateOutOfBoundTest(yearAndMonth: String) {
        // given
        // when
        // then
        assertThatThrownBy {
            RaemianLocalDateUtil.of(yearAndMonth, yearAndMonth)
        }.isInstanceOf(RuntimeException::class.java)
    }

    @Test
    @DisplayName("LocalDate Format을 'YYYY.MM'로 변환할 수 있다.")
    fun localDateFormattingTest() {
        // given
        val localDate = LocalDate.now()

        // when
        val formattedLocalDate = localDate.format()

        // then
        val year = localDate.year.toString()
        var month = localDate.monthValue.toString()
        if (month.length == 1) {
            month = "0$month"
        }

        val expectedLocalDate = "$year.$month"

        assertThat(formattedLocalDate).isEqualTo(expectedLocalDate)
    }
}
