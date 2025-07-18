package com.tae.backend.chat.domain

import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
class Chat(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        val userId: Long,
        @Column(nullable = false)
        val question: String,
        @Column
        var answer: String?,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "thread_id", nullable = false)
        val thread: Thread,
        @Column
        val createAt: ZonedDateTime = ZonedDateTime.now(),

        @Enumerated
        var status: ChatStatus
){
        fun success(answer : String){
                this.answer = answer
                this.status = ChatStatus.COMPLETED
        }
        fun fail(){
                this.status = ChatStatus.FAILED
        }
}
