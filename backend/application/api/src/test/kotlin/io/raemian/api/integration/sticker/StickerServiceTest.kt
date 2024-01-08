package io.raemian.api.integration.sticker

import io.raemian.api.sticker.StickerService
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.sticker.StickerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StickerServiceTest {

    @Autowired
    private lateinit var stickerService: StickerService

    @Autowired
    private lateinit var stickerRepository: StickerRepository

    @Test
    @DisplayName("저장된 전체 Tag를 조회할 수 있다.")
    fun findAllByUserIdTest() {
        // given
        val sticker1 = Sticker("sticker", "image1")
        val sticker2 = Sticker("sticker2", "image2")

        stickerRepository.save(sticker1)
        stickerRepository.save(sticker2)

        // when
        val stickers = stickerService.findAll()

        // then
        assertAll(
            Executable {
                assertThat(stickers.size).isEqualTo(2)
                assertThat(stickers[0].name).isEqualTo(sticker1.name)
                assertThat(stickers[0].url).isEqualTo(sticker1.url)
                assertThat(stickers[1].name).isEqualTo(sticker2.name)
                assertThat(stickers[1].url).isEqualTo(sticker2.url)
            },
        )
    }
}
