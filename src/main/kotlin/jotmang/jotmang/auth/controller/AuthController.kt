package jotmang.jotmang.auth.controller

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
        @RequestParam state: String
    ): ResponseEntity<LoginResponse> {
        val response = authService.handleCallback(code, state)
        return ResponseEntity.ok(response)
    }
}
