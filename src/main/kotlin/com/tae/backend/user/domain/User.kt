package com.tae.backend.user.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user")
class User(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "userId")
        val id: Long = 0,
        @Column
        val email: String,
        @Column
        val password: String,
        @Column
        val name: String,
        @Column
        val createAt: LocalDateTime,
        @Enumerated(EnumType.STRING)
        @Column
        var role: Role
){
    companion object{
        fun create(email: String, password: String, name: String, role: Role): User {
            return User(
                    email = email,
                    password = password,
                    name = name,
                    role = role,
                    createAt = LocalDateTime.now()
            )
        }
    }
}