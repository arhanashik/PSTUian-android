package com.workfort.pstuian.model;

enum class CheckInPrivacy(val value: String) {
    PUBLIC("public"),
    ONLY_ME("only_me"), ;

    companion object {
        fun create(value: String) = entries.firstOrNull { it.value == value }
    }
}