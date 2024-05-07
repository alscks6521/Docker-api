package com.daelim.database.service

import com.daelim.database.core.dto.UserDto
import com.daelim.database.core.entity.UserEntity
import com.daelim.database.repository.UserRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UserService(
    private val userRepository: UserRepository,
    private val redisTemplate: StringRedisTemplate // Redis 템플릿 추가
) {
    fun registerUser(username: String, password: String): UserDto {
        userRepository.findByUsername(username)?.let {
            throw IllegalStateException("Username already exists")
        }
        val newUser = UserEntity(username = username, password = password)
        return userRepository.save(newUser).let {
            UserDto(username = it.username)
        }
    }

    fun validateUser(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username)
        return user?.password == password
    }

    fun createSession(username: String): String {
        val sessionId = generateSessionId(username)
        redisTemplate.opsForValue().set("session:$username", sessionId, 30, TimeUnit.MINUTES) // 세션 유효 시간 30분
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
