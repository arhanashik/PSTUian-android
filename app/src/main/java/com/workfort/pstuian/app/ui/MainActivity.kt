package com.workfort.pstuian.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.common.navigation.AppNavHost
import com.workfort.pstuian.ui.common.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _root_ide_package_.com.workfort.pstuian.ui.common.theme.AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    _root_ide_package_.com.workfort.pstuian.ui.common.navigation.AppNavHost(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
