package com.workfort.pstuian.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.Foundation.NSBundle
import platform.Foundation.NSUUID
import platform.UIKit.UIDevice
import platform.posix.uname
import platform.posix.utsname
import kotlin.experimental.ExperimentalNativeApi

class IOSPlatformInfo : PlatformInfo {
    override val appVersion: String
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

    override val platformName: String
        get() = "${UIDevice.currentDevice.systemName} ${UIDevice.currentDevice.systemVersion}"

    override val locale: String
        get() = "en" // Default to English for now to avoid compilation issues

    override fun getLocalIpAddress(): String {
        return "127.0.0.1" // Simplified for now
    }

//    override fun vibrate() {
//        UIImpactFeedbackGenerator(platform.UIKit.UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium).apply {
//            prepare()
//            impactOccurred()
//        }
//    }
}