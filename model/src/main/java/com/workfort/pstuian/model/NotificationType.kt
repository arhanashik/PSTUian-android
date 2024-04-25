package com.workfort.pstuian.model;

enum class NotificationType(val value: String) {
    DEFAULT("default"),
    BLOOD_DONATION("blood_donation"),
    NEWS("news"),
    HELP("help"), ;

    companion object {
        fun create(value: String) = entries.firstOrNull { it.value == value }
    }
}