package com.eduplan.domain.model

enum class UserRole(val authority: String) {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    companion object {
        fun fromAuthority(authority: String): UserRole? =
            entries.firstOrNull() { it.authority == authority }
    }
}