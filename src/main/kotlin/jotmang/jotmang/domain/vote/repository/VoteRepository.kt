package jotmang.jotmang.domain.vote.repository

import jotmang.jotmang.domain.vote.entity.Vote
import org.springframework.data.jpa.repository.JpaRepository

interface VoteRepository : JpaRepository<Vote, Long> {
    fun existsByUserId(userId: Long): Boolean
}
