package jotmang.jotmang.auth.service

import jotmang.jotmang.auth.dto.LoginRequest
import jotmang.jotmang.auth.dto.LoginResponse
import jotmang.jotmang.auth.dto.SignUpRequest
import jotmang.jotmang.auth.util.JwtUtil
import jotmang.jotmang.domain.user.entity.User
import jotmang.jotmang.domain.user.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun signUp(request: SignUpRequest) {
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("이미 사용 중인 아이디입니다.")
        }
        userRepository.save(
            User(
                username = request.username,
                password = passwordEncoder.encode(request.password),
                name = request.name
            )
        )
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.")
        }

        val token = jwtUtil.generateToken(user.id, user.username, user.name)
        return LoginResponse(
            accessToken = token,
            userId = user.id,
            name = user.name,
            username = user.username
        )
    }
}