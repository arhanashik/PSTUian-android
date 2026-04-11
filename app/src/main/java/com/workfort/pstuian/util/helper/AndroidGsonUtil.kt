package com.workfort.pstuian.util.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class AndroidGsonUtil: GsonUtil {
    override fun toJson(obj: Any): String {
        return Gson().toJson(obj)
    }

    override fun<T> fromJson(jsonString: String): T {
        return Gson().fromJson(jsonString, getType<T>())
    }

    fun<T> fromJson(jsonStr: String, any: Class<T>): T {
        return Gson().fromJson(jsonStr, any)
    }

    fun <T> getType(): Type = object: TypeToken<T>() {}.type
}