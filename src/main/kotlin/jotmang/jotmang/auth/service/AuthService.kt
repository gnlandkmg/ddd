package jotmang.jotmang.auth.service

import jotmang.jotmang.auth.dto.DAuthTokenResponse
import jotmang.jotmang.auth.dto.DAuthUserInfo
import jotmang.jotmang.auth.dto.DAuthUserResponse
import jotmang.jotmang.auth.dto.LoginResponse
import jotmang.jotmang.auth.util.JwtUtil
import jotmang.jotmang.domain.user.entity.User
import jotmang.jotmang.domain.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.Base64
import java.util.concurrent.ConcurrentHashMap

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
    private val restClient: RestClient,
    @Value("\${dauth.client-id}") private val clientId: String,
    @Value("\${dauth.client-secret}") private val clientSecret: String,
    @Value("\${dauth.redirect-uri}") private val redirectUri: String,
    @Value("\${dauth.authorize-url}") private val authorizeUrl: String,
    @Value("\${dauth.token-url}") private val tokenUrl: String,
    @Value("\${dauth.user-info-url}") private val userInfoUrl: String,
    @Value("\${dauth.scope}") private val scope: String
) {
    // state -> codeVerifier (CSRF 방지 및 PKCE 검증용)
    private val pkceStore = ConcurrentHashMap<String, String>()

    fun generateDAuthUrl(): String {
        val codeVerifier = generateCodeVerifier()
        val codeChallenge = generateCodeChallenge(codeVerifier)
        val state = generateRandomString(16)

        pkceStore[state] = codeVerifier

        return buildString {
            append(authorizeUrl)
            append("?response_type=code")
            append("&client_id=${encode(clientId)}")
            append("&redirect_uri=${encode(redirectUri)}")
            append("&scope=${encode(scope)}")
            append("&state=${encode(state)}")
            append("&code_challenge=${encode(codeChallenge)}")
            append("&code_challenge_method=S256")
        }
    }

    fun handleCallback(code: String, state: String): LoginResponse {
        val codeVerifier = pkceStore.remove(state)
            ?: throw IllegalStateException("유효하지 않은 state 파라미터입니다.")

        val tokenResponse = exchangeCodeForToken(code, codeVerifier)
        val userInfo = fetchUserInfo(tokenResponse.accessToken)
        val user = saveOrUpdateUser(userInfo)
        val jwt = jwtUtil.generateToken(user.id, user.username, user.name, user.role)

        return LoginResponse(
            accessToken = jwt,
            userId = user.id,
            name = user.name,
            username = user.username,
            role = user.role
        )
    }

    private fun exchangeCodeForToken(code: String, codeVerifier: String): DAuthTokenResponse =
        restClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply {
                add("grant_type", "authorization_code")
                add("code", code)
                add("redirect_uri", redirectUri)
                add("client_id", clientId)
                add("client_secret", clientSecret)
                add("code_verifier", codeVerifier)
            })
            .retrieve()
            .body(DAuthTokenResponse::class.java)
            ?: throw RuntimeException("DAuth 토큰 교환에 실패했습니다.")

    private fun fetchUserInfo(accessToken: String): DAuthUserInfo {
        val response = restClient.get()
            .uri(userInfoUrl)
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body(DAuthUserResponse::class.java)
            ?: throw RuntimeException("DAuth 사용자 정보 조회에 실패했습니다.")
        return response.data
    }

    private fun saveOrUpdateUser(userInfo: DAuthUserInfo): User {
        val role = userInfo.roles.firstOrNull() ?: "UNKNOWN"
        val existing = userRepository.findByPublicId(userInfo.publicId)
        return if (existing != null) {
            existing.username = userInfo.username
            existing.name = userInfo.name
            existing.role = role
            existing.updatedAt = LocalDateTime.now()
            userRepository.save(existing)
        } else {
            userRepository.save(
                User(
                    publicId = userInfo.publicId,
                    username = userInfo.username,
                    name = userInfo.name,
                    role = role
                )
            )
        }
    }

    private fun generateCodeVerifier(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun generateCodeChallenge(codeVerifier: String): String {
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(codeVerifier.toByteArray(Charsets.US_ASCII))
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
    }

    private fun generateRandomString(byteLength: Int): String {
        val bytes = ByteArray(byteLength)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun encode(value: String): String =
        URLEncoder.encode(value, StandardCharsets.UTF_8)
}
