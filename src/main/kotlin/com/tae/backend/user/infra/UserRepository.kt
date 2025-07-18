package com.tae.backend.user.infra

import com.tae.backend.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository :JpaRepository<User,Long> {
}