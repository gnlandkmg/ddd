package jotmang.jotmang.domain.candidate.repository

import jotmang.jotmang.domain.candidate.entity.Candidate
import org.springframework.data.jpa.repository.JpaRepository

interface CandidateRepository : JpaRepository<Candidate, Long>
