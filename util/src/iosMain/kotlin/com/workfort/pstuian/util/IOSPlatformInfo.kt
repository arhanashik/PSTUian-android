package com.workfort.pstuian.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSUUID
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier
import platform.UIKit.UIDevice
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
            return runCatching {
                val key = "device_id"
                val existing = KeychainHelper.getString(key)
                if (existing != null) return existing

                val newId = NSUUID().UUIDString
                KeychainHelper.setString(key, newId)
                newId
            }.getOrNull() ?: NSUUID().UUIDString
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

    override suspend fun getFcmToken(): String? {
        // Implement FCM token retrieval for iOS if needed, or return null for now
        return null
    }
}