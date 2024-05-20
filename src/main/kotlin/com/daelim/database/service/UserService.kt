package com.daelim.database.service

import com.daelim.database.core.entity.UserEntity
import com.daelim.database.repository.UserRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UserService(
    private val userRepository: UserRepository,
    private val redisTemplate: StringRedisTemplate
) {
    

    fun registerUser(username: String, password: String): UserEntity {
        // 비밀번호 길이 검증
        if (password.length < 8) {
            throw IllegalArgumentException("비밀번호는 8자리 이상 되어야 해요.")
        }


        // 중복 사용자 확인
        val existingUser = userRepository.findByUsername(username)
        if (existingUser != null) {
            throw IllegalStateException("Username already exists")
        }

        /**
         * 1. 상위 스코프에서 암호화 변수 선언 private val passwordEncoder = BCryptPasswordEncoder()
         * 비밀번호 단방향 해시 함수를 사용하여 암호화해야 합니다.
         * BCryptPasswordEncoder를 사용하여 다음과 같이 암호화할 수 있습니다.
         * 2. val encodedPassword = BCryptPasswordEncoder().encode(password)
         */
        val user = UserEntity(username = username, password = password)
        return userRepository.save(user)
    }

    
    fun validateUser(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username)

        // 입력된 비밀번호와 데이터베이스에 저장된 해시 값을 비교하여 인증을 수행
        return user?.let { passwordEncoder.matches(password, user.password) } ?: false
    }

    fun createSession(username: String): String {
        val sessionId = generateSessionId(username)
        redisTemplate.opsForValue().set("session:$username", sessionId, 30, TimeUnit.MINUTES)
        return sessionId
    }

    fun checkSession(username: String, sessionId: String): Boolean {
        val storedSessionId = redisTemplate.opsForValue().get("session:$username")
        return sessionId == storedSessionId
    }

    private fun generateSessionId(username: String): String {
        return username.hashCode().toString() + System.currentTimeMillis().toString()
    }
}
