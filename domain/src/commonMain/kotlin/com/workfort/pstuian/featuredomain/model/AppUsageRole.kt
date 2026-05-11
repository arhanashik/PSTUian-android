package com.workfort.pstuian.featuredomain.model

enum class AppUsageRole(val storageValue: String) {
    TEACHER("teacher"),
    STUDENT("student"),
    EMPLOYEE("employee"),
    VISITOR("visitor");

    companion object {
        fun fromStorage(value: String?): AppUsageRole? = entries.firstOrNull { it.storageValue == value }
    }
}
