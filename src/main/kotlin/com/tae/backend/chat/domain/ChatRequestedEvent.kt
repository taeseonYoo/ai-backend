package com.tae.backend.chat.domain

data class ChatRequestedEvent(
        val chatId: Long,
        val isStreaming: Boolean,
        val model: String?
)