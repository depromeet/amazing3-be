package io.raemian.api.integration.user

import io.raemian.api.user.UserService
import io.raemian.api.user.controller.UpdateGoalsPublicRequest
import io.raemian.storage.db.core.user.Authority
import io.raemian.storage.db.core.user.User
import io.raemian.storage.db.core.user.enums.OAuthProvider
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
class UserServiceTest {

    companion object {
        val USER_FIXTURE = User(
            email = "dfghcvb111@naver.com",
            userName = "binaryHoHo",
            nickname = "binaryHoHoHo",
            image = "",
            birth = LocalDate.MIN,
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
            isGoalsPublic = true,
        )
    }

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun saveEntities() {
        entityManager.merge(USER_FIXTURE)
    }

    @Test
    @DisplayName("User의 isGoalsPublic을 수정할 수 있다.")
    fun updateGoalsPulbic() {
        // given
        val isGoalsPublicBeforeUpdate = USER_FIXTURE.isGoalsPublic
        val updateGoalsPublicRequest = UpdateGoalsPublicRequest(isGoalsPublicBeforeUpdate.not())

        // when
        userService.updateGoalsPublic(
            userId = USER_FIXTURE.id!!,
            updateGoalsPublicRequest,
        )

        // then
        val user = entityManager.find(User::class.java, USER_FIXTURE.id!!)
        assertThat(isGoalsPublicBeforeUpdate).isNotEqualTo(user.isGoalsPublic)
    }
}
