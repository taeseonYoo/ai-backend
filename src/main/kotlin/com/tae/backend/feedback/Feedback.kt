package com.tae.backend.feedback

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
@Table
class Feedback(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @Column(nullable = false)
        val userId: String,

        @Column(nullable = false)
        val chatId: String,

        @Column(nullable = false)
        val isPositive: Boolean,

        @Column(nullable = false)
        val createdAt: ZonedDateTime = ZonedDateTime.now(),

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var status: FeedbackStatus = FeedbackStatus.PENDING
)