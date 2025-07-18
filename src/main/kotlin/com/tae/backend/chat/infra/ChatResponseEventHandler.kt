package com.tae.backend.chat.infra

import com.tae.backend.chat.domain.ChatRequestedEvent
import com.tae.backend.chat.application.ChatResponseService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

//이벤트 수신
@Component
class ChatResponseEventHandler(
        private val chatResponseService: ChatResponseService
) {
    //트랜잭션 커밋 이후, 비동기 실행
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: ChatRequestedEvent){
        chatResponseService.process(event.chatId,event.isStreaming, event.model)
    }
}