package io.raemian.storage.db.core

import io.raemian.storage.db.CoreDbContextTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExampleRepositoryIT(
    val exampleRepository: ExampleRepository,
) : CoreDbContextTest() {
    @Test
    fun testShouldBeSavedAndFound() {
        val saved = exampleRepository.save(ExampleEntity(1L, "SPRING_BOOT"))
        assertThat(saved.exampleColumn).isEqualTo("SPRING_BOOT")

        val found = exampleRepository.findById(saved.id!!).get()
        assertThat(found.exampleColumn).isEqualTo("SPRING_BOOT")
    }
}
