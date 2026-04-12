package com.workfort.pstuian.model

import kotlinx.serialization.Serializable

@Serializable
enum class FacultySelectionMode(val mode: Int) {
    FACULTY(0),
    BATCH(1),
    BOTH(2),
    NONE(-1);

    companion object {
        fun create(type: Int) = entries.firstOrNull { it.mode == type }
    }
}