package com.workfort.pstuian.util.helper

import platform.UIKit.UIDevice
import platform.Foundation.NSLocale
import platform.Foundation.languageCode
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

actual object PlatformUtil {

    actual val deviceVersionName: String = UIDevice.currentDevice.systemVersion

    actual val appVersionCode: Int = 0 // Needs proper iOS implementation

    actual val appVersionName: String = "" // Needs proper iOS implementation

    actual fun getLocaleLanguage(): String = "en"

    actual fun getDeviceName(): String = UIDevice.currentDevice.name

    actual fun getLocalIpAddress(): String {
        // Implement iOS specific IP address lookup if needed
        return ""
    }

    actual fun vibrate() {
        val generator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
        generator.prepare()
        generator.impactOccurred()
    }
}
