package com.workfort.pstuian.util.helper

interface GsonUtil {
    fun toJson(obj: Any): String

    fun<T> fromJson(jsonString: String): T
}