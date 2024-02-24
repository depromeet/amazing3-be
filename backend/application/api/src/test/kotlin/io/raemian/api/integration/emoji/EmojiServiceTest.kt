package io.raemian.api.integration.emoji

import io.raemian.api.emoji.EmojiService
import io.raemian.storage.db.core.emoji.Emoji
import io.raemian.storage.db.core.emoji.EmojiRepository
import io.raemian.storage.db.core.emoji.ReactedEmoji
import io.raemian.storage.db.core.emoji.ReactedEmojiRepository
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.enums.OAuthProvider
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
class EmojiServiceTest {

    companion object {
        private val USER_FIXTURE = User(
            email = "dfghcvb111@naver.com",
            username = "binaryHoHo",
            nickname = "binaryHoHoHo",
            birth = LocalDate.MIN,
            image = "",
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
        )

        private val USER_FIXTURE2 = User(
            email = "dfghcvb2@naver.com",
            username = "binaryHo",
            nickname = "binaryHo",
            birth = LocalDate.MIN,
            image = "",
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
        )

        private val LIFE_MAP_FIXTURE = LifeMap(USER_FIXTURE, true)
        private val STICKER_FIXTURE = Sticker("sticker", "image yeah")
        private val TAG_FIXTURE = Tag("태그")
        private val GOAL_FIXTURE = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "제목",
            deadline = LocalDate.now(),
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "내용",
        )
    }

    @Autowired
    private lateinit var emojiService: EmojiService

    @Autowired
    private lateinit var emojiRepository: EmojiRepository

    @Autowired
    private lateinit var reactedEmojiRepository: ReactedEmojiRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun saveEntities() {
        entityManager.merge(USER_FIXTURE)
        entityManager.merge(USER_FIXTURE2)
        entityManager.merge(LIFE_MAP_FIXTURE)
        entityManager.merge(STICKER_FIXTURE)
        entityManager.merge(TAG_FIXTURE)
        entityManager.merge(GOAL_FIXTURE)
    }

    @Test
    @DisplayName("전체 이모지 리스트를 조회할 수 있다.")
    fun findAllTest() {
        // given
        val emoji = Emoji("이모지", "url")
        val emoji2 = Emoji("이모지2", "url2")
        emojiRepository.save(emoji)
        emojiRepository.save(emoji2)

        // when
        val emojis = emojiService.findAll()

        // then
        assertThat(emojis.size).isEqualTo(2)
    }

    @Test
    @DisplayName("Goal에 달린 이모지들을 조회할 수 있다.")
    fun findGoalReactedEmojisTest() {
        // given
        val emoji = Emoji("이모지", "url")
        val emoji2 = Emoji("이모지", "url")
        emojiRepository.saveAll(listOf(emoji, emoji2))

        val reactedEmoji = ReactedEmoji(GOAL_FIXTURE, emoji, USER_FIXTURE)
        val reactedEmoji2 = ReactedEmoji(GOAL_FIXTURE, emoji, USER_FIXTURE2)
        val reactedEmoji3 = ReactedEmoji(GOAL_FIXTURE, emoji2, USER_FIXTURE)
        reactedEmojiRepository.saveAll(listOf(reactedEmoji, reactedEmoji2, reactedEmoji3))

        // when
        val reactedEmojis = emojiService.findGoalReactedEmojis(GOAL_FIXTURE.id!!)

        // then
        assertThat(reactedEmojis.totalReactedEmojisCount).isEqualTo(3)
        println(reactedEmojis)
    }

    @Test
    @DisplayName("Goal에 이모지를 반응할 수 있다.")
    fun reactTest() {
        // given
        val emoji = Emoji("이모지", "url")
        emojiRepository.save(emoji)

        // when
        emojiService.react(
            emojiId = emoji.id!!,
            goalId = GOAL_FIXTURE.id!!,
            emojiReactUserId = USER_FIXTURE.id!!,
        )

        // then
        val reactedEmojis = reactedEmojiRepository.findAllByGoal(GOAL_FIXTURE)
        assertThat(reactedEmojis.size).isEqualTo(1)
        assertThat(reactedEmojis.get(0).emoji).isEqualTo(emoji)
    }

    @Test
    @DisplayName("하나의 Goal에 같은 유저가 같은 이모지를 여러번 달아도 1개만 저장된다.")
    fun reactDuplicatedReactTest() {
        // given
        val emoji = Emoji("이모지", "url")
        emojiRepository.save(emoji)

        // when
        repeat(5) {
            emojiService.react(
                emojiId = emoji.id!!,
                goalId = GOAL_FIXTURE.id!!,
                emojiReactUserId = USER_FIXTURE.id!!,
            )
            entityManager.clear()
        }

        // then
        val reactedEmojis = reactedEmojiRepository.findAllByGoal(GOAL_FIXTURE)
        assertThat(reactedEmojis.size).isEqualTo(1)
    }

    @Test
    @DisplayName("하나의 Goal에 같은 유저가 같은 이모지를 여러번 달아도, Entity 중복 예외가 발생되지 않는다.")
    fun reactDuplicatedReactDoesNotThrowDuplicatedExceptionTest() {
        // given
        val emoji = Emoji("이모지", "url")
        emojiRepository.save(emoji)

        // when
        val reactEmoji = {
            emojiService.react(
                emojiId = emoji.id!!,
                goalId = GOAL_FIXTURE.id!!,
                emojiReactUserId = USER_FIXTURE.id!!,
            )
            entityManager.clear()
        }

        // then
        assertDoesNotThrow {
            repeat(5) { reactEmoji() }
        }
    }

    @Test
    @DisplayName("반응한 이모지를 삭제할 수 있다.")
    fun removeTest() {
        // given
        val emoji = Emoji("이모지", "url")
        emojiRepository.save(emoji)

        // when
        emojiService.remove(
            emojiId = emoji.id!!,
            goalId = GOAL_FIXTURE.id!!,
            emojiReactUserId = USER_FIXTURE.id!!,
        )

        // then
        val reactedEmojis = reactedEmojiRepository.findAllByGoal(GOAL_FIXTURE)
        assertThat(reactedEmojis.size).isEqualTo(0)
    }

    @Test
    @DisplayName("반응한 이모지를 여러번 삭제해도 예외가 발생하지 않는다.")
    fun removeIdempotentTest() {
        // given
        val emoji = Emoji("이모지", "url")
        emojiRepository.save(emoji)

        // when, then
        assertDoesNotThrow {
            repeat(5) {
                emojiService.remove(
                    emojiId = emoji.id!!,
                    goalId = GOAL_FIXTURE.id!!,
                    emojiReactUserId = USER_FIXTURE.id!!,
                )
            }
        }
        val reactedEmojis = reactedEmojiRepository.findAllByGoal(GOAL_FIXTURE)
        assertThat(reactedEmojis.size).isEqualTo(0)
    }
}
