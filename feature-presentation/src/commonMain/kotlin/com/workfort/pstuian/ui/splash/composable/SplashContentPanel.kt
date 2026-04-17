package com.workfort.pstuian.ui.splash.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.CardWithAnimatedBorder
import com.workfort.pstuian.ui.common.composable.TitleTextMedium
import com.workfort.pstuian.ui.splash.state.SplashUiEvent
import com.workfort.pstuian.ui.splash.state.SplashUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.app_name

@Composable
fun SplashContentPanel(
    state: SplashUiState,
    onEvent: (SplashUiEvent) -> Unit,
) {
    val borderColors = listOf(
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.primary,
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            CardWithAnimatedBorder(
                borderColors = borderColors,
                borderSize = 2.dp,
            ) {
                TitleTextMedium(
                    text = stringResource(Res.string.app_name),
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(text = state.loadingText)
    }
}
