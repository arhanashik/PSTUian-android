package com.workfort.pstuian.ui.splash.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.usecase.InitialScreenState
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle

@Composable
internal fun ErrorContent(
    screenState: InitialScreenState,
    statusText: String,
    descriptionText: String?,
    actionBtnText: String?,
    onClickAction: () -> Unit,
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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

            if (actionBtnText != null) {
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onClickAction) {
                    Text(text = actionBtnText)
                }
            }
        }
    }
}