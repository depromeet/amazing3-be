package io.raemian.api

import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FailTest {

    @Test
    fun MergeRule_적용_확인을_위한_항상_실패하는_테스트() {
        fail()
    }
}
