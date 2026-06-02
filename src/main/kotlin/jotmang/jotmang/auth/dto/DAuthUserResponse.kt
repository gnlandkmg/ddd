package jotmang.jotmang.auth.dto

data class DAuthUserResponse(
    val status: Int,
    val message: String,
    val data: DAuthUserInfo
)

data class DAuthUserInfo(
    val publicId: String,
    val username: String,
    val name: String,
    val phone: String?,
    val roles: List<String>
)
