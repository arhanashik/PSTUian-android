package com.workfort.pstuian

import androidx.compose.ui.window.ComposeUIViewController
import com.workfort.pstuian.common.navigation.AppNavHost
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
    }
) {
    AppNavHost()
}
