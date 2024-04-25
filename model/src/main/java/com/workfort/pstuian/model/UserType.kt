package com.workfort.pstuian.model;

enum class UserType(val type: String) {
    STUDENT("student"),
    TEACHER("teacher"), ;

    companion object {
        fun create(type: String) = entries.firstOrNull { it.type == type }
    }
}