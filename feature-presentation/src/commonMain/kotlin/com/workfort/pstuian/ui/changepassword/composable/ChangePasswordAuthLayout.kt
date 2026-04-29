package com.workfort.pstuian.ui.changepassword.composable

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordScreenPanel
import com.workfort.pstuian.ui.signin.composable.CompactAuthHeader
import com.workfort.pstuian.ui.signin.composable.ForgotPasswordHeaderContent
import com.workfort.pstuian.ui.signin.screendata.SectionCornerRadius

/**
 * Same structure as sign-in auth: green header (crossfading) and white form section with shared scroll.
 */
@Composable
internal fun ChangePasswordAuthForeground(
    greenHeight: Dp,
    panel: ChangePasswordScreenPanel,
    changePasswordHeaderTitle: String,
    onBackFromChangePanel: () -> Unit,
    onBackFromResetPanel: () -> Unit,
    formContent: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        val navigationBarInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val minWhiteHeight = (maxHeight - greenHeight - navigationBarInset).coerceAtLeast(0.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .verticalScroll(scrollState)
                .navigationBarsPadding(),
        ) {
            val greenShape = RoundedCornerShape(bottomEnd = SectionCornerRadius)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(greenHeight),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(greenShape)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = greenShape,
                        ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding(),
                    ) {
                        Crossfade(
                            targetState = panel,
                            animationSpec = tween(durationMillis = 300),
                            modifier = Modifier.fillMaxSize(),
                            label = "changePasswordHeader",
                        ) { currentPanel ->
                            when (currentPanel) {
                                ChangePasswordScreenPanel.ChangePassword ->
                                    CompactAuthHeader(
                                        title = changePasswordHeaderTitle,
                                        onBack = onBackFromChangePanel,
                                    )
                                ChangePasswordScreenPanel.ResetPassword ->
                                    ForgotPasswordHeaderContent(onBack = onBackFromResetPanel)
                            }
                        }
                    }
                }
            }

            val whiteShape = RoundedCornerShape(topStart = SectionCornerRadius)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minWhiteHeight)
                    .clip(whiteShape)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = whiteShape,
                    ),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    formContent()
                }
            }
        }
    }
}
