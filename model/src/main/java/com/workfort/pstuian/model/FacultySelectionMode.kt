package com.workfort.pstuian.model;

enum class FacultySelectionMode(val mode: Int) {
    FACULTY(0),
    BATCH(1),
    BOTH(2), ;

    companion object {
        fun create(type: Int) = entries.firstOrNull { it.mode == type }
    }
}