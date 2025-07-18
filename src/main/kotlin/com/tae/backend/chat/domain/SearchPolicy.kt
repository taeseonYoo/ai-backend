package com.tae.backend.chat.domain

interface SearchPolicy {
    fun hasSearchPermission(): Boolean;
}