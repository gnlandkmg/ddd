package jotmang.jotmang.vote.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import jotmang.jotmang.auth.service.AuthenticatedUserService
import jotmang.jotmang.vote.dto.VoteRequest
import jotmang.jotmang.vote.dto.VoteResponse
import jotmang.jotmang.vote.service.VoteService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/votes")
class VoteController(
    private val authenticatedUserService: AuthenticatedUserService,
    private val voteService: VoteService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun vote(
        @Valid @RequestBody request: VoteRequest,
        httpRequest: HttpServletRequest
    ): VoteResponse {
        val voter = authenticatedUserService.getCurrentUser(httpRequest)
        return voteService.vote(request, voter)
    }
}
