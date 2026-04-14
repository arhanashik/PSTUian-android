package com.workfort.pstuian.util.helper

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class JsonParser {

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    // ✅ Serialize
    inline fun <reified T> toJson(obj: T): String {
        return json.encodeToString(obj)
    }

    // ✅ Deserialize (reified - recommended)
    inline fun <reified T> fromJson(jsonString: String): T {
        return json.decodeFromString(jsonString)
    }

    // ✅ Deserialize with explicit serializer (advanced use)
    fun <T> fromJson(jsonString: String, serializer: KSerializer<T>): T {
        return json.decodeFromString(serializer, jsonString)
    }

    // ✅ Dynamic parsing (like Gson JsonObject)
    fun parse(jsonString: String): JsonElement {
        return json.parseToJsonElement(jsonString)
    }
}