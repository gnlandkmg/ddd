package jotmang.jotmang.domain.vote.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jotmang.jotmang.domain.candidate.entity.Candidate
import jotmang.jotmang.domain.user.entity.User
import java.time.LocalDateTime

@Entity
@Table(
    name = "votes",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_votes_user", columnNames = ["user_id"])
    ]
)
class Vote(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    val candidate: Candidate,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
