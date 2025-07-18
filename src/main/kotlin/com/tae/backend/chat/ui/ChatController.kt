package com.tae.backend.chat.ui

import com.tae.backend.chat.ui.dto.ChatRequest
import com.tae.backend.chat.ui.dto.ChatResponse
import com.tae.backend.chat.application.ChatService
import com.tae.backend.chat.ui.dto.ChatResponseDto
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chats")
class ChatController (
        private val chatService : ChatService
){
    /**
     * AI로 질문을 요청한다.
     */
    @PostMapping
    fun createChat(
            @RequestBody request : ChatRequest,
            @RequestParam(required = false, defaultValue = "false") isStreaming: Boolean,
            @RequestParam(required = false) model: String?,
            userId :Long
    ): ResponseEntity<ChatResponse>{
        val saved = chatService.save(request, userId,isStreaming,model);
        return ResponseEntity.ok(saved)
    }

    @GetMapping("/history")
    fun getHistory(
            userId: Long,
            pageable: Pageable,
            @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<List<ChatResponseDto>> {

        val history = chatService.getChatsGroupedByThread(userId,pageable.sort,pageable)
        return ResponseEntity.ok(history)
    }

    @PostMapping("/{threadId}/delete")
    fun deleteThread(
            @PathVariable threadId: Long,
            @AuthenticationPrincipal userDetails: UserDetails
    ):ResponseEntity<Void>{
        chatService.deleteThread(threadId,userDetails.username)
        return ResponseEntity.noContent().build()
    }
}