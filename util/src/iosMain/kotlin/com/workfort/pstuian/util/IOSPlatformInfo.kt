package com.workfort.pstuian.util

import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSUUID
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier
import platform.UIKit.UIDevice
import platform.darwin.freeifaddrs
import platform.darwin.getifaddrs
import platform.darwin.ifaddrs
import platform.posix.AF_INET
import platform.posix.NI_MAXHOST
import platform.posix.NI_NUMERICHOST
import platform.posix.getnameinfo
import platform.posix.uname
import platform.posix.utsname
import kotlin.experimental.ExperimentalNativeApi

class IOSPlatformInfo : PlatformInfo {
    override val appVersionCode: Int
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleVersion") as? Int ?: 0

    override val appVersionName: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0.0"

    @OptIn(ExperimentalNativeApi::class)
    override val isDebug: Boolean
        get() = Platform.isDebugBinary

    override val storeUrl: String
        get() = "https://apps.apple.com/app/id6761439692"

    override val deviceId: String
        get() {
            val key = "device_id"
            val userDefaults = platform.Foundation.NSUserDefaults.standardUserDefaults
            val existing = userDefaults.stringForKey(key)
            if (existing != null) return existing

            val newId = NSUUID().UUIDString
            userDefaults.setObject(newId, forKey = key)
            return newId
        }

    @OptIn(ExperimentalForeignApi::class)
    override val model: String
        get() = memScoped {
            val systemInfo = alloc<utsname>()
            uname(systemInfo.ptr)
            systemInfo.machine.toKString()
        }

    override val platform: String
        get() = "${UIDevice.currentDevice.systemName} ${UIDevice.currentDevice.systemVersion}"

    override val locale: String
        get() = NSLocale.currentLocale.localeIdentifier
}