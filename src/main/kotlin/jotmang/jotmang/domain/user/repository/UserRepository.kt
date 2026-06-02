package jotmang.jotmang.domain.user.repository

import jotmang.jotmang.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByPublicId(publicId: String): User?
}
