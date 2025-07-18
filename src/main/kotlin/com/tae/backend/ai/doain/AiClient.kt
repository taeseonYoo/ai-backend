package com.tae.backend.ai.doain

interface AiClient {
    fun ask(prompt: String,isStreaming: Boolean,model:String?): String
}