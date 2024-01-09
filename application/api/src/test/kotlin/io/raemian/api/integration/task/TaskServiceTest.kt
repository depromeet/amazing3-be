package io.raemian.api.integration.task

import io.raemian.api.lifemap.LifeMap
import io.raemian.api.task.TaskService
import io.raemian.api.task.controller.request.CreateTaskRequest
import io.raemian.api.task.controller.request.RewriteTaskRequest
import io.raemian.api.task.controller.request.UpdateTaskCompletionRequest
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.task.Task
import io.raemian.storage.db.core.task.TaskRepository
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
class TaskServiceTest {

    companion object {
        val USER_FIXTURE = User(
            email = "dfghcvb111@naver.com",
            username = "binaryHoHo",
            nickname = "binaryHoHoHo",
            image = "",
            birth = LocalDate.MIN,
            provider = OAuthProvider.NAVER,
            authority = Authority.ROLE_USER,
        )

        val LIFE_MAP_FIXTURE = LifeMap(USER_FIXTURE, true)
        val STICKER_FIXTURE = Sticker("sticker", "image yeah")
        val TAG_FIXTURE = Tag("꿈")
        val GOAL_FIXTURE = Goal(
            lifeMap = LIFE_MAP_FIXTURE,
            title = "title",
            deadline = LocalDate.MAX,
            sticker = STICKER_FIXTURE,
            tag = TAG_FIXTURE,
            description = "description",
            tasks = emptyList(),
        )
    }

    @Autowired
    private lateinit var taskService: TaskService

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun saveEntities() {
        entityManager.merge(USER_FIXTURE)
        entityManager.merge(LIFE_MAP_FIXTURE)
        entityManager.merge(STICKER_FIXTURE)
        entityManager.merge(TAG_FIXTURE)
        goalRepository.save(GOAL_FIXTURE)
    }

    @Test
    @DisplayName("Goal ID와 description으로 Task를 생성할 수 있다.")
    fun createTest() {
        // given
        // when
        val response = taskService.create(
            currentUserId = USER_FIXTURE.id!!,
            CreateTaskRequest(GOAL_FIXTURE.id!!, "description"),
        )

        // then
        val task = taskRepository.getById(response.id)
        assertThat(task.id).isEqualTo(response.id)
        assertThat(task.description).isEqualTo("description")
        assertThat(task.goal.description).isEqualTo(GOAL_FIXTURE.description)
    }

    @Test
    @DisplayName("Task 생성시 isDone 값은 false로 설정된다.")
    fun createTaskIsDoneFalseTest() {
        // given
        // when
        val response = taskService.create(
            currentUserId = USER_FIXTURE.id!!,
            CreateTaskRequest(GOAL_FIXTURE.id!!, "description"),
        )

        // then
        val task = taskRepository.getById(response.id)
        assertThat(task.isDone).isEqualTo(false)
    }

    @Test
    @DisplayName("Task의 Description을 수정할 수 있다.")
    fun rewriteTest() {
        // given
        val description = "description"
        val newTask = Task.createTask(GOAL_FIXTURE, description)
        taskRepository.save(newTask)

        // when
        val newDescription = "new description"
        taskService.rewrite(
            currentUserId = USER_FIXTURE.id!!,
            taskId = newTask.id!!,
            RewriteTaskRequest(newDescription),
        )

        // then
        val task = taskRepository.getById(newTask.id!!)
        assertThat(task.description).isEqualTo(newDescription)
    }

    @Test
    @DisplayName("Task의 수행 여부를 변경할 수 있다.")
    fun updateTaskCompletionTest() {
        // given
        val newTask = Task.createTask(GOAL_FIXTURE, "description")
        taskRepository.save(newTask)

        // when
        taskService.updateTaskCompletion(
            currentUserId = USER_FIXTURE.id!!,
            taskId = newTask.id!!,
            UpdateTaskCompletionRequest(true),
        )

        // then
        val task = taskRepository.getById(newTask.id!!)
        assertThat(task.isDone).isEqualTo(true)
    }

    @Test
    @DisplayName("Task를 삭제할 수 있다.")
    fun deleteTest() {
        // given
        val newTask = Task.createTask(GOAL_FIXTURE, "description")
        taskRepository.save(newTask)

        // when
        taskService.delete(USER_FIXTURE.id!!, newTask.id!!)

        // then
        val task = taskRepository.findById(newTask.id!!)
        assertThat(task.isEmpty).isTrue()
    }
}
