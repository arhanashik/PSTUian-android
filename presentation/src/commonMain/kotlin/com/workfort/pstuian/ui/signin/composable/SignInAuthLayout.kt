package com.workfort.pstuian.ui.signin.composable

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
import androidx.compose.foundation.layout.padding
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
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.SectionCornerRadius

/**
 * Green header (crossfading top bar + hero) and white form section with safe-area padding.
 * The entire column scrolls as one unit so the keyboard can push the full layout up.
 */
@Composable
internal fun SignInAuthForeground(
    greenHeight: Dp,
    panel: AuthPanel,
    showStudentTeacherToggle: Boolean,
    authUserTypeForForms: UserType,
    onAuthUserTypeForFormsChange: (UserType) -> Unit,
    onSkip: () -> Unit,
    onBackToSignIn: () -> Unit,
    formContent: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        // Because the scroll container applies `navigationBarsPadding()`, we must account for that
        // inset when sizing the white section. Otherwise the content becomes slightly taller than
        // the viewport even when nothing overflows, causing a small "phantom" scroll.
        val navigationBarInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val minWhiteHeight = (maxHeight - greenHeight - navigationBarInset).coerceAtLeast(0.dp)
        // Primary behind the scroll column so the white card’s rounded top-start wedge shows green.
        // The green header stacks surface (white) under primary: the bottom-end radius cuts through
        // primary so that corner reads as white — the form/surface color — not more green.
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
                            label = "headerContent",
                        ) { currentPanel ->
                            when (currentPanel) {
                                AuthPanel.SignIn -> SignInHeaderContent(onSkip = onSkip)
                                AuthPanel.StudentSignUp, AuthPanel.TeacherSignUp ->
                                    SignUpHeaderContent(onBack = onBackToSignIn)
                                AuthPanel.ForgotPassword -> ForgotPasswordHeaderContent(onBack = onBackToSignIn)
                                AuthPanel.EmailVerification -> EmailVerificationHeaderContent(onBack = onBackToSignIn)
                            }
                        }

                        SignInHeroReveal(visible = panel == AuthPanel.SignIn)
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
                    if (showStudentTeacherToggle) {
                        val toggleIndex = when (authUserTypeForForms) {
                            UserType.STUDENT -> 0
                            UserType.TEACHER -> 1
                            UserType.EMPLOYEE -> 0
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                                .padding(top = 16.dp, bottom = 16.dp),
                        ) {
                            ToggleSwitch(
                                options = listOf("Student", "Teacher"),
                                selectedIndex = toggleIndex,
                                onSelectedIndexChange = { index ->
                                    val type = if (index == 0) UserType.STUDENT else UserType.TEACHER
                                    onAuthUserTypeForFormsChange(type)
                                },
                            )
                        }
                    }
                    formContent()
                }
            }
        }
    }
}
