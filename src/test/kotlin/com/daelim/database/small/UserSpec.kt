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

    // @BeforeEach: 코드가 시작하기 전에 미리 돌린다.
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

        // When 패스워드가 이것이.
        val password = randomUser.password

        // Then 패스워드가 8,16 줄인지 확인
        assertTrue(password.length in 8..16)
    }

//    ### **Given-When-Then 접근법**
//
//    Given-When-Then은 행동 주도 개발(Behavior-Driven Development, BDD)의 일부로, 소프트웨어 개발에서 사용되는 시나리오 기반 테스트 방식입니다. 이 접근법은 테스트 코드의 가독성과 유지 관리를 향상시키기 위해 자연 언어에 가까운 구조를 사용하여 테스트 사례를 명확하게 설명합니다.
//
//    - **Given**은 테스트 사전 조건을 설정합니다. 이는 테스트가 실행되기 전에 시스템이 어떤 상태에 있어야 하는지를 명시합니다.
//    - **When**은 테스트를 실행할 특정 조건이나 사용자의 행동을 설명합니다. 이 단계에서는 주로 하나의 행동이 포함됩니다.
//    - **Then**은 예상 결과를 확인합니다. 이는 주어진 조건하에서 시스템이 어떻게 반응해야 하는지를 검증합니다.

}