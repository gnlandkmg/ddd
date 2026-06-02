package jotmang.jotmang.auth.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jotmang.jotmang.auth.dto.LoginResponse
import jotmang.jotmang.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @GetMapping("/login")
    fun login(): ResponseEntity<Void> {
        val url = authService.generateDAuthUrl()
        return ResponseEntity.status(302)
            .header("Location", url)
            .build()
    }

    @GetMapping("/veify")
    fun callback(
        @RequestParam code: String,
        @RequestParam state: String,
        response: HttpServletResponse
    ): ResponseEntity<LoginResponse> {
        val loginResponse = authService.handleCallback(code, state)

        Cookie("accessToken", loginResponse.accessToken).apply {
            isHttpOnly = true
            secure = false   // HTTPS 환경에서는 true로 변경
            path = "/"
            maxAge = 86400   // 24시간 (초 단위)
        }.also { response.addCookie(it) }

        return ResponseEntity.ok(loginResponse)
    }
}
