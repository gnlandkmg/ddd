package jotmang.jotmang.auth.dto

data class LoginResponse(
    val accessToken: String,
    val userId: Long,
    val name: String,
    val username: String
)