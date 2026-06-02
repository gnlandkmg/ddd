package jotmang.jotmang.vote.dto

import jotmang.jotmang.candidate.dto.CandidateResponse
import jotmang.jotmang.domain.user.entity.User
import jotmang.jotmang.domain.vote.entity.Vote
import java.time.LocalDateTime

data class VoteResponse(
    val id: Long,
    val voter: VoterResponse,
    val candidate: CandidateResponse,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(vote: Vote): VoteResponse =
            VoteResponse(
                id = vote.id,
                voter = VoterResponse.from(vote.user),
                candidate = CandidateResponse.from(vote.candidate),
                createdAt = vote.createdAt
            )
    }
}

data class VoterResponse(
    val id: Long,
    val username: String,
    val name: String,
    val role: String
) {
    companion object {
        fun from(user: User): VoterResponse =
            VoterResponse(
                id = user.id,
                username = user.username,
                name = user.name,
                role = user.role
            )
    }
}
