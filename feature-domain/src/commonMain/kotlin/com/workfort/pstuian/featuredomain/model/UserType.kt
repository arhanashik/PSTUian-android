package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserType(val type: String) {
    STUDENT("student"),
    TEACHER("teacher");

    companion object {
        fun create(type: String) = entries.firstOrNull { it.type == type }
    }
}