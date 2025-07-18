package com.tae.backend.chat.infra


import com.tae.backend.chat.domain.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : JpaRepository<Chat, Long> {
    fun findByThreadIdOrderByCreateAt(threadId: Long): List<Chat>
}
