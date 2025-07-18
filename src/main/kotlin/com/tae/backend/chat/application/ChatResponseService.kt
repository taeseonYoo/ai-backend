package com.tae.backend.chat.application

import com.tae.backend.ai.doain.AiClient
import com.tae.backend.chat.infra.ChatRepository
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChatResponseService(
        private val chatRepository: ChatRepository,
        private val aiClient: AiClient
){
    @Retryable(
            value = [Exception::class],
            maxAttempts = 3,
            backoff = Backoff(delay = 2000)
    )
    @Transactional
    fun process(chatId: Long,isStreaming: Boolean,model: String?) {
        val chat = chatRepository.findById(chatId)
                .orElseThrow { IllegalArgumentException("Chat not found") }
        try {
            val answer = aiClient.ask(chat.question,isStreaming,model)
            chat.success(answer)
        } catch (e: Exception) {
            chat.fail()
        }

    }

}
