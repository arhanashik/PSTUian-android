package com.workfort.pstuian.util.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 12:23 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

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

//    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}