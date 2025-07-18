package com.tae.backend.ai.infra

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.tae.backend.ai.doain.AiClient
import com.tae.backend.chat.ui.dto.OpenAiResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class OpenAiClient(
        @Value("\${openai.api.key}")
        private val apiKey: String
) : AiClient {
    private val objectMapper = jacksonObjectMapper()
    private val webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    private val defaultModel = "gpt-3.5-turbo"

    override fun ask(prompt: String,isStreaming: Boolean,model: String?): String {

        val requestBody = mapOf(
                "model" to (model ?: defaultModel),
                "stream" to isStreaming,
                "messages" to listOf(
                        mapOf("role" to "user", "content" to prompt)
                )
        )

        return if (isStreaming) {
            val sb = StringBuilder()
            webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToFlux(String::class.java)
                    .mapNotNull { extractContentFromStreamChunk(it) }
                    .doOnNext { sb.append(it) }
                    .blockLast() // Wait for stream to complete

            sb.toString()
        } else {
            val response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(OpenAiResponse::class.java)
                    .block()!!

            response.choices.first().message.content
        }

    }
    private fun extractContentFromStreamChunk(chunk: String): String? {
        return try {
            val cleaned = chunk.removePrefix("data:").trim()
            if (cleaned == "[DONE]") return null

            val json: JsonNode = objectMapper.readTree(cleaned)
            json["choices"]?.get(0)?.get("delta")?.get("content")?.asText()
        } catch (e: Exception) {
            null
        }
    }
}