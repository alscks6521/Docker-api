package com.daelim.database.large

import com.daelim.database.controller.UserController
import com.daelim.database.core.dto.UserDto
import com.daelim.database.core.entity.UserEntity
import com.daelim.database.service.UserService
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@WebMvcTest(UserController::class)
class UserControllerSpec @Autowired constructor(
    private val mockMvc: MockMvc
) {
    @MockBean
    private lateinit var userService: UserService

    private val faker = Faker()

    private lateinit var randomUser: UserEntity

    @BeforeEach
    fun setup() {
        val username = faker.internet().username()
        val password = faker.internet().password(8, 16)
        randomUser = UserEntity(
            username = username,
            password = password
        )
    }

    @Test
    @DisplayName("회원 가입 API - 성공")
    fun `회원 가입 API - 성공`() {
        val user = randomUser
        `when`(userService.registerUser(randomUser.username, randomUser.password)).thenReturn(user)
        mockMvc.perform(
            post("/users/register") // 엔드포인트 경로 수정
                .param("username", user.username)
                .param("password", user.password)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value(user.username))
    }

    @Test
    @DisplayName("로그인 API - 성공")
    fun `로그인 API - 성공`() {
        val sessionId = "validSessionId"
        `when`(userService.validateUser(randomUser.username, randomUser.password)).thenReturn(true)
        `when`(userService.createSession(randomUser.username)).thenReturn(sessionId)
        mockMvc.perform(
            post("/users/login") // 엔드포인트 경로 수정
                .param("username", randomUser.username)
                .param("password", randomUser.password)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().string(sessionId))
    }

    @Test
    @DisplayName("세션 체크 API - 성공")
    fun `세션 체크 API - 성공`() {
        val sessionId = "validSessionId"
        `when`(userService.checkSession(randomUser.username, sessionId)).thenReturn(true)
        mockMvc.perform(
            get("/users/check") // 엔드포인트 경로 수정
                .param("username", randomUser.username)
                .param("sessionId", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().string("Session valid"))
    }
}