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
    private val passwordEncoder = BCryptPasswordEncoder()

    fun registerUser(username: String, password: String): UserEntity {
        if (password.length < 8) {
            throw IllegalArgumentException("비밀번호는 8자리 이상 되어야 해요.")
        }

        val existingUser = userRepository.findByUsername(username)
        if (existingUser != null) {
            throw IllegalStateException("Username already exists")
        }

        // BCryptPasswordEncoder를 사용하여 비밀번호를 암호화합니다.
        // registerUser 메서드 내에서 passwordEncoder.encode(password)를 호출하여 비밀번호를 암호화한 뒤, User 객체를 생성하고 저장합니다.
        val encodedPassword = passwordEncoder.encode(password)
        val user = UserEntity(username = username, password = encodedPassword)
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