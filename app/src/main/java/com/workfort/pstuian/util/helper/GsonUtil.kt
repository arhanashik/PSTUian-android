package com.workfort.pstuian.util.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object GsonUtil {
    fun toJson(obj: Any): String {
        return Gson().toJson(obj)
    }

    fun<T> fromJson(jsonStr: String, any: Class<T>): T {
        return Gson().fromJson(jsonStr, any)
    }

    inline fun<reified T> fromJson(jsonString: String): T {
        return Gson().fromJson(jsonString, getType<T>())
    }

    inline fun <reified T> getType(): Type = object: TypeToken<T>() {}.type
}