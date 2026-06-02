package jotmang.jotmang.candidate.controller

import jakarta.validation.Valid
import jotmang.jotmang.candidate.dto.CandidateResponse
import jotmang.jotmang.candidate.dto.RegisterCandidateRequest
import jotmang.jotmang.candidate.service.CandidateService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/candidates")
class CandidateController(
    private val candidateService: CandidateService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registerCandidate(
        @Valid @RequestBody request: RegisterCandidateRequest
    ): CandidateResponse = candidateService.register(request)

    @GetMapping
    fun getCandidates(): List<CandidateResponse> = candidateService.findAll()
}
