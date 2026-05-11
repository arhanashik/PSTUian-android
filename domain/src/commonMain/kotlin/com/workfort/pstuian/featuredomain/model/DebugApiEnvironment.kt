package com.workfort.pstuian.featuredomain.model

enum class DebugApiEnvironment(val storageValue: String, val displayLabel: String) {
    LOCAL("local", "Local Server"),
    DEV("dev", "Development Sever"),
    PROD("prod", "Production Server");

    companion object {
        fun fromStorageValue(value: String?): DebugApiEnvironment =
            entries.find { it.storageValue == value } ?: LOCAL
    }
}
