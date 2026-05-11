package com.workfort.pstuian.featuredomain.model;

enum class ThemeMode {
    System, Light, Dark;

    companion object {
        fun fromName(value: String?): ThemeMode {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: System
        }
    }
}