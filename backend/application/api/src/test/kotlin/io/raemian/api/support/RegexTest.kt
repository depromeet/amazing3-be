package io.raemian.api.support

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RegexTest {

    @Test
    @DisplayName("유저네임 영문+숫자+하이픈 으로만 생성 가능하도록 체크")
    fun validateUsername() {
        val regex = "^[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*".toRegex()
        val result1 = regex.matches("aaaaaa00000")
        val result2 = regex.matches("아아아아아아")
        val result3 = regex.matches("-aaaa")
        val result4 = regex.matches("aaaa-")
        val result5 = regex.matches("----")


        println(result1)
        println(result2)
        println(result3)
        println(result4)
        println(result5)

    }
}