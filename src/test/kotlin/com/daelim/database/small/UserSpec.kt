package com.daelim.database.small

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
class UserSpec {
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
    @DisplayName("8~16 자리의 비밀 번호")
    fun `8~16 자리의 비밀 번호`() {
        // Given - setup()에서 이미 처리됨

        // When
        val password = randomUser.password

        // Then
        assertTrue(password.length in 8..16)
    }

}