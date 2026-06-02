package jotmang.jotmang.vote.dto

import jakarta.validation.constraints.Positive

data class VoteRequest(
    @field:Positive(message = "후보자 ID는 양수여야 합니다.")
    val candidateId: Long
)
