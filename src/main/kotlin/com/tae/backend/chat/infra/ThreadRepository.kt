package com.tae.backend.chat.infra

import com.tae.backend.chat.domain.Thread
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ThreadRepository:JpaRepository<Thread,Long> {
    fun findTopByUserIdOrderByCreateAtDesc(userId: Long): Thread?

    fun findByUserId(userId: Long,sort: Sort): List<Thread>
}