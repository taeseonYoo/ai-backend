package com.tae.backend.chat.ui.dto

data class ChatResponse (
    val id: Long,
    val prompt: String,
    val status: String
)