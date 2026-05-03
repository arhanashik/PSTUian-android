package com.workfort.pstuian.ui.splash.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.usecase.InitialScreenState
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.app_name

@Composable
internal fun ErrorContent(
    screenState: InitialScreenState,
    statusText: String,
    descriptionText: String?,
    showContinueAnyway: Boolean = false,
    onClickAction: () -> Unit,
    onContinueAnywayClick: () -> Unit = {},
) {
    val actionBtnText = splashErrorPrimaryActionLabel(screenState)
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut(),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = statusText,
                    style = TextStyle.title2.copy(MaterialTheme.colorScheme.error),
                )

                if (screenState is InitialScreenState.Maintenance) {
                    Spacer(modifier = Modifier.height(8.dp))
                    MaintenanceTimer(screenState.remainingTime)
                }

                if (descriptionText != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = descriptionText,
                        style = TextStyle.label1.copy(AppColors.textSecondary),
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (actionBtnText != null) {
                        Button(onClick = onClickAction) {
                            Text(text = actionBtnText)
                        }
                    }
                    if (showContinueAnyway) {
                        Spacer(modifier = Modifier.width(12.dp))
                        OutlinedButton(onClick = onContinueAnywayClick) {
                            Text(text = "Continue Anyway")
                        }
                    }
                }
            }

            Text(
                text = stringResource(Res.string.app_name),
                style = TextStyle.title1.copy(color = AppColors.textPrimary),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            )
        }
    }
}

private fun splashErrorPrimaryActionLabel(screenState: InitialScreenState): String? =
    when (screenState) {
        is InitialScreenState.MissingDeviceInfo -> "Retry"
        is InitialScreenState.MissingConfig -> "Refresh"
        is InitialScreenState.Maintenance -> "Refresh"
        is InitialScreenState.ForceUpdate -> "Update"
        is InitialScreenState.DeviceBlocklisted -> null
        is InitialScreenState.Home -> null
    }
