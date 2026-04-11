package com.workfort.pstuian.util.helper

expect object PlatformUtil {
    val deviceVersionName: String
    val appVersionCode: Int
    val appVersionName: String
    fun getLocaleLanguage(): String
    fun getDeviceName(): String
    fun getLocalIpAddress(): String
    fun vibrate()
}
