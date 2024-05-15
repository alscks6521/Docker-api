package com.daelim.database.controller

import com.daelim.database.core.dto.UserDto
import com.daelim.database.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    @PostMapping("/register")
    @Operation(summary = "회원 가입 API", description = "새로운 사용자를 등록합니다.")
    fun register(
        @RequestParam username: String,
        @RequestParam password: String
    ): ResponseEntity<UserDto> {
        return try {
            val user = userService.registerUser(username, password)
            ResponseEntity.ok(UserDto(user.username))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(UserDto(errorMessage = e.message))
        } catch (e: IllegalStateException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(UserDto(errorMessage = e.message))
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "사용자 인증을 수행하고 세션을 생성합니다.")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String
    ): ResponseEntity<String> {
        return if (userService.validateUser(username, password)) {
            val sessionId = userService.createSession(username)
            ResponseEntity.ok(sessionId)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @GetMapping("/check")
    @Operation(summary = "세션 체크 API", description = "사용자의 세션 유효성을 확인합니다.")
    fun check(
        @RequestParam username: String,
        @RequestParam sessionId: String
    ): ResponseEntity<String> {
        return if (userService.checkSession(username, sessionId)) {
            ResponseEntity.ok("Session valid")
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }
}