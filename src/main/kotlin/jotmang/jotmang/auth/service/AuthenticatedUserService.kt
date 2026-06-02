package jotmang.jotmang.auth.service

import jakarta.servlet.http.HttpServletRequest
import jotmang.jotmang.auth.util.JwtUtil
import jotmang.jotmang.domain.user.entity.User
import jotmang.jotmang.domain.user.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthenticatedUserService(
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository
) {
    fun getCurrentUser(request: HttpServletRequest): User {
        val token = extractToken(request)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.")

        if (!jwtUtil.validateToken(token)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.")
        }

        val userId = jwtUtil.getUserId(token)
        return userRepository.findById(userId)
            .orElseThrow { ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자 정보를 찾을 수 없습니다.") }
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.trim()

        if (!bearerToken.isNullOrBlank()) {
            return bearerToken
        }

        return request.cookies
            ?.firstOrNull { it.name == "accessToken" }
            ?.value
            ?.takeIf { it.isNotBlank() }
    }
}
