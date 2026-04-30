package com.workfort.pstuian.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.AppViewModel
import com.workfort.pstuian.ui.common.navigation.AppNavHost
import com.workfort.pstuian.util.deeplink.DeepLinkParser
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    val appViewModel: AppViewModel by viewModel()
    private val deepLinkParser: DeepLinkParser by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel.onLaunchDeepLink(
            savedInstanceRestored = savedInstanceState != null,
            deepLinkAction = deepLinkParser.parse(intent?.dataString),
        )
        enableEdgeToEdge()
        setContent {
            val appTheme by appViewModel.appTheme.collectAsState()

            AppNavHost(theme = appTheme)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        appViewModel.onNewIntentDeepLink(deepLinkParser.parse(intent.dataString))
    }
}