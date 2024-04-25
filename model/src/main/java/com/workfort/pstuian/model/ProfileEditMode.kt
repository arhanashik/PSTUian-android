package com.workfort.pstuian.model;

enum class ProfileEditMode(val mode: Int) {
    ACADEMIC(0), 
    CONNECT(1), ;

    companion object {
        fun create(mode: Int) = entries.firstOrNull { it.mode == mode }
    }
}