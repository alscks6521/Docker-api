package com.daelim.database.medium

import com.daelim.database.core.entity.UserEntity
import com.daelim.database.repository.UserRepository
import com.daelim.database.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

@SpringBootTest
class UserServiceSpec {
    @Autowired
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userRepository: UserRepository

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

    // 중복된 사용자가 있는지 확인하고 실패 tdd 함수
    @Test
    @DisplayName("중복된 사용자가 있는지 확인하고 회원가입 실패")
    fun `중복된 사용자가 있느지 확인하고 회원가입 실패`() {
        // Given
        given(userRepository.findByUsername(randomUser.username)).willReturn(randomUser)

        // When
        val exception = assertThrows(IllegalStateException::class.java) {
            userService.registerUser(randomUser.username, randomUser.password)
        }

        // Then
        assertEquals("Username already exists", exception.message)
    }


    // 중복된 사용자가 있는지 확인하고 성공 tdd 함수

  
}
