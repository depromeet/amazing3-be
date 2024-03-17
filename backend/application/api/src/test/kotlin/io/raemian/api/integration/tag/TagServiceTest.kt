package io.raemian.api.integration.tag

import io.raemian.api.tag.service.TagService
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.tag.TagRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TagServiceTest {

    @Autowired
    private lateinit var tagService: TagService

    @Autowired
    private lateinit var tagRepository: TagRepository

    @Test
    @DisplayName("저장된 전체 Sticker를 조회할 수 있다.")
    fun findAllByUserIdTest() {
        // given
        val tag1 = Tag("tag1")
        val tag2 = Tag("tag2")

        tagRepository.save(tag1)
        tagRepository.save(tag2)

        // when
        val tags = tagService.findAll()

        // then
        assertAll(
            Executable {
                assertThat(tags.size).isEqualTo(2)
                assertThat(tags[0].content).isEqualTo(tag1.content)
                assertThat(tags[1].content).isEqualTo(tag2.content)
            },
        )
    }
}
