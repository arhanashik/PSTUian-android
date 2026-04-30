package com.workfort.pstuian.ui.common.navigation

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

private val routeQueryJson = Json { ignoreUnknownKeys = true }
private val stringStringMapSerializer = MapSerializer(String.serializer(), String.serializer())

/** Encodes route/deep-link query params map as JSON. */
fun encodeRouteQueryParams(params: Map<String, String>): String =
    routeQueryJson.encodeToString(stringStringMapSerializer, params)

/** Decodes JSON route argument into a map; never throws. */
fun decodeRouteQueryParams(jsonString: String): Map<String, String> =
    try {
        if (jsonString.isBlank()) emptyMap()
        else routeQueryJson.decodeFromString(stringStringMapSerializer, jsonString)
    } catch (_: Exception) {
        emptyMap()
    }
