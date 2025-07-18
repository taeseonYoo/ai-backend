package com.tae.backend.chat.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
@Table(name = "threads")
class Thread(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        val userId: Long,

        @Column(nullable = false)
        val createdAt: ZonedDateTime = ZonedDateTime.now(),

        @OneToMany(mappedBy = "thread", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        val chats: List<Chat> = mutableListOf()
)