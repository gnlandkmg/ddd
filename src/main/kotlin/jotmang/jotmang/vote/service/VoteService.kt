package jotmang.jotmang.vote.service

import jotmang.jotmang.domain.candidate.repository.CandidateRepository
import jotmang.jotmang.domain.user.entity.User
import jotmang.jotmang.domain.vote.entity.Vote
import jotmang.jotmang.domain.vote.repository.VoteRepository
import jotmang.jotmang.vote.dto.VoteRequest
import jotmang.jotmang.vote.dto.VoteResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class VoteService(
    private val voteRepository: VoteRepository,
    private val candidateRepository: CandidateRepository
) {
    @Transactional
    fun vote(request: VoteRequest, voter: User): VoteResponse {
        if (voteRepository.existsByUserId(voter.id)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "이미 투표한 사용자입니다.")
        }

        val candidate = candidateRepository.findById(request.candidateId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "후보자를 찾을 수 없습니다.") }

        val vote = Vote(
            user = voter,
            candidate = candidate
        )

        return VoteResponse.from(voteRepository.save(vote))
    }
}
