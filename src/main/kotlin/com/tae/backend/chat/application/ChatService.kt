package com.tae.backend.chat.application

import com.tae.backend.chat.domain.*
import com.tae.backend.chat.infra.ChatRepository
import com.tae.backend.chat.ui.dto.ChatResponse
import com.tae.backend.chat.ui.dto.ChatRequest
import com.tae.backend.chat.infra.ThreadRepository
import com.tae.backend.chat.ui.dto.ChatDto
import com.tae.backend.chat.ui.dto.ChatResponseDto
import com.tae.backend.user.infra.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class ChatService(
        private val chatRepository: ChatRepository,
        private val threadRepository: ThreadRepository,
        private val userRepository: UserRepository,
        private val eventPublisher: ApplicationEventPublisher,
        private val searchPolicy : SearchPolicy
) {
    @Transactional
    fun save(request: ChatRequest, userId: Long, isStreaming: Boolean, model: String?): ChatResponse {

        val lastThread = threadRepository.findTopByUserIdOrderByCreateAtDesc(userId)

        val someThread = if (lastThread == null || lastThread.createdAt.isBefore(ZonedDateTime.now().minusMinutes(30))) {
            threadRepository.save(Thread(userId = userId))
        } else {
            lastThread
        }

        val chat = Chat(
                userId = userId,
                question = request.prompt,
                answer = null,
                thread = someThread,
                status = ChatStatus.PENDING
        )
        val savedChat = chatRepository.save(chat)

        //이벤트로 ai에 요청
        eventPublisher.publishEvent(ChatRequestedEvent(savedChat.id, isStreaming, model))
        return ChatResponse(savedChat.id, savedChat.question, savedChat.status.name)
    }


    @Transactional(readOnly = true)
    fun getChatsGroupedByThread(userId: Long,sort:Sort,pageable:Pageable):List<ChatResponseDto>{

        val threads = threadRepository.findByUserId(userId, sort)
        //권한 검사
        if(!searchPolicy.hasSearchPermission()){
            throw IllegalArgumentException("권한이 없습니다.")
        }

        return threads
                .drop(pageable.offset.toInt())
                .take(pageable.pageSize)
                .map { thread ->
                    val chats = chatRepository.findByThreadIdOrderByCreateAt(thread.id)
                            .map { chat ->
                                ChatDto(chat.id, chat.question, chat.answer, chat.createAt)
                            }
                    ChatResponseDto(thread.id, chats)
                }
    }

    @Transactional
    fun deleteThread(threadId: Long, userInfo: String) {
        val findThread = threadRepository.findById(threadId).orElseThrow { IllegalArgumentException("쓰레드가 없습니다.") }


        threadRepository.delete(findThread)
    }
}