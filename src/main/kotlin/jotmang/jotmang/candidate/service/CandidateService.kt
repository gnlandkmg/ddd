package jotmang.jotmang.candidate.service

import jotmang.jotmang.candidate.dto.CandidateResponse
import jotmang.jotmang.candidate.dto.RegisterCandidateRequest
import jotmang.jotmang.domain.candidate.entity.Candidate
import jotmang.jotmang.domain.candidate.repository.CandidateRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CandidateService(
    private val candidateRepository: CandidateRepository
) {
    @Transactional
    fun register(request: RegisterCandidateRequest): CandidateResponse {
        val candidate = Candidate(
            name = request.name.trim(),
            district = request.district.trim(),
            pledge = request.pledge.trim(),
            introduction = request.introduction?.trim()?.takeIf { it.isNotEmpty() }
        )

        return CandidateResponse.from(candidateRepository.save(candidate))
    }

    @Transactional(readOnly = true)
    fun findAll(): List<CandidateResponse> =
        candidateRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
            .map { CandidateResponse.from(it) }
}
