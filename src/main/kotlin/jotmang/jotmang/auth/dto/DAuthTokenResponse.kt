package jotmang.jotmang.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class DAuthTokenResponse(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("expires_in") val expiresIn: Int,
    @JsonProperty("refresh_token") val refreshToken: String?
)
