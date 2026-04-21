package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp

/**
 * Decorative layer behind the real auth UI: surface strip matching the green header height and
 * primary color filling the rest (covers rounded corners during transitions).
 */
@Composable
internal fun SignInAuthBackdrop(greenHeight: Dp) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(greenHeight)
                .background(MaterialTheme.colorScheme.surface),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary),
        )
    }
}

/**
 * Green header (crossfading top bar + hero) and white form section with safe-area padding.
 */
@Composable
internal fun SignInAuthForeground(
    greenHeight: Dp,
    panel: AuthPanel,
    onSkip: () -> Unit,
    onBackToSignIn: () -> Unit,
    formContent: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(greenHeight)
                .clip(RoundedCornerShape(bottomEnd = SectionCornerRadius))
                .background(MaterialTheme.colorScheme.primary),
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
                    label = "headerContent",
                ) { currentPanel ->
                    when (currentPanel) {
                        AuthPanel.SignIn -> SignInHeaderContent(onSkip = onSkip)
                        AuthPanel.SignUp -> SignUpHeaderContent(onBack = onBackToSignIn)
                        AuthPanel.ForgotPassword -> ForgotPasswordHeaderContent(onBack = onBackToSignIn)
                        AuthPanel.EmailVerification -> EmailVerificationHeaderContent(onBack = onBackToSignIn)
                    }
                }

                SignInHeroReveal(visible = panel == AuthPanel.SignIn)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = SectionCornerRadius))
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Box(modifier = Modifier.navigationBarsPadding()) {
                formContent()
            }
        }
    }
}
