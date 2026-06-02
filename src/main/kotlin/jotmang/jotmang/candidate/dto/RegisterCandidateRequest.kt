package jotmang.jotmang.candidate.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterCandidateRequest(
    @field:NotBlank(message = "후보자 이름은 필수입니다.")
    @field:Size(max = 50, message = "후보자 이름은 50자 이하여야 합니다.")
    val name: String,

    @field:NotBlank(message = "선거구는 필수입니다.")
    @field:Size(max = 100, message = "선거구는 100자 이하여야 합니다.")
    val district: String,

    @field:NotBlank(message = "공약은 필수입니다.")
    @field:Size(max = 500, message = "공약은 500자 이하여야 합니다.")
    val pledge: String,

    @field:Size(max = 1000, message = "소개는 1000자 이하여야 합니다.")
    val introduction: String? = null
)
