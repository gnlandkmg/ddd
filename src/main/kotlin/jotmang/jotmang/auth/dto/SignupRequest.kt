package jotmang.jotmang.auth.dto

data class SignUpRequest(
    val username: String,
    val password: String,
    val name: String
)