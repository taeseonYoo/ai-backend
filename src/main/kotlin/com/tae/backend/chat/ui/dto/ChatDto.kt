package com.tae.backend.chat.ui.dto

import java.time.ZonedDateTime

class ChatDto (
    val chatId: Long,
    val question: String,
    val answer: String?,
    val createdAt: ZonedDateTime
)