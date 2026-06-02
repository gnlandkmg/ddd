package jotmang.jotmang.candidate.dto

import jotmang.jotmang.domain.candidate.entity.Candidate
import java.time.LocalDateTime

data class CandidateResponse(
    val id: Long,
    val name: String,
    val district: String,
    val pledge: String,
    val introduction: String?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(candidate: Candidate): CandidateResponse =
            CandidateResponse(
                id = candidate.id,
                name = candidate.name,
                district = candidate.district,
                pledge = candidate.pledge,
                introduction = candidate.introduction,
                createdAt = candidate.createdAt
            )
    }
}
