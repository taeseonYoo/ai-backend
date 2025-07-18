package com.tae.backend.user.application

import com.tae.backend.user.domain.Role
import com.tae.backend.user.domain.User
import com.tae.backend.user.infra.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) {

    fun register(email: String, rawPassword: String, name: String): User {
        val encodedPassword = passwordEncoder.encode(rawPassword)
        val user = User.create(email = email, password = encodedPassword, name = name, role = Role.MEMBER)
        return userRepository.save(user)
    }

}