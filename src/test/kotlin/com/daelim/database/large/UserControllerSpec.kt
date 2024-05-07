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
        val password = faker.internet().password()
        randomUser = UserEntity(
            username = username,
            password = password
        )
    }

    @Test
    @DisplayName("회원 가입 api")
    fun `회원 가입 api`() {
        val user = randomUser
        `when`(userService.registerUser(randomUser.username, randomUser.password)).thenReturn(
            UserDto( username = user.username )
        )

        mockMvc.perform(
            post("/register")
                .param("username", user.username)
                .param("password", user.password)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value(user.username))
    }
}
