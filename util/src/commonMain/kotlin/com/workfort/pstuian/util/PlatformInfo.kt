package com.workfort.pstuian.util

interface PlatformInfo {
    val appVersionCode: Int
    val appVersionName: String
    val isDebug: Boolean
    val storeUrl: String
    val deviceId: String
    val model: String
    val platform: String
    val locale: String
}

fun isVersionLower(current: String, required: String): Boolean {
    val currentParts = current.split(".").mapNotNull { it.toIntOrNull() }
    val requiredParts = required.split(".").mapNotNull { it.toIntOrNull() }
    val size = maxOf(currentParts.size, requiredParts.size)
    for (i in 0 until size) {
        val curr = currentParts.getOrElse(i) { 0 }
        val req = requiredParts.getOrElse(i) { 0 }
        if (curr < req) return true
        if (curr > req) return false
    }
    return false
}
