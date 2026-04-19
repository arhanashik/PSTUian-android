package com.workfort.pstuian

import androidx.compose.ui.window.ComposeUIViewController
import com.workfort.pstuian.ui.common.navigation.AppNavHost
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
    }
) {
    AppNavHost()
}
