package jotmang.jotmang.auth.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jotmang.jotmang.auth.dto.LoginRequest
import jotmang.jotmang.auth.dto.LoginResponse
import jotmang.jotmang.auth.dto.SignUpRequest
import jotmang.jotmang.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<Void> {
        authService.signUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<LoginResponse> {
        val loginResponse = authService.login(request)

        Cookie("accessToken", loginResponse.accessToken).apply {
            isHttpOnly = true
            secure = false
            path = "/"
            maxAge = 86400
        }.also { response.addCookie(it) }

        return ResponseEntity.ok(loginResponse)
    }
}