package com.workfort.pstuian.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.workfort.pstuian.app.ui.common.theme.AppTheme
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.helper.Toaster

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(navController = navController)
                }
            }
        }
        observeConnectivity()
    }

    override fun onResume() {
        super.onResume()

        /**
         * Sometimes if the app is in onPause() for long time
         * the network status returns wrong state.
         * So, onResume() just refresh the state
         */
        NetworkUtil.refresh()
    }

    private fun observeConnectivity() {
        NetworkUtil.from(this).observe(this) { connected ->
            if (connected.not()) {
                Toaster.show("Lost Data connection!")
            }
        }
    }
}
