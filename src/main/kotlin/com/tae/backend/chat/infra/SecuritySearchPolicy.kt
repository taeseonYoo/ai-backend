package com.tae.backend.chat.infra

import com.tae.backend.chat.domain.SearchPolicy
import com.tae.backend.chat.domain.Thread
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecuritySearchPolicy :SearchPolicy{
    override fun hasSearchPermission(): Boolean {
        return isCurrentUserAdminRole()
    }

    private fun isCurrentUserAdminRole(): Boolean {
        val context = SecurityContextHolder.getContext() ?: return false
        val authentication = context.authentication ?: return false
        val authorities = authentication.authorities ?: return false
        return authorities.any { it.authority == "ROLE_ADMIN" }
    }


}