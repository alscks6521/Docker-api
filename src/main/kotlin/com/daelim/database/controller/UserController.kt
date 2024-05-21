package com.daelim.database.controller

import com.daelim.database.core.dto.UserDto
import com.daelim.database.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {
    @PostMapping("/users/register")
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

    @PostMapping("/users/login")
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

    @GetMapping("/users/check")
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