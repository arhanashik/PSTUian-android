package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.signin.state.SignUpFormData
import org.jetbrains.compose.resources.painterResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.ic_logo

private val SectionCornerRadius = 56.dp
private val CompactHeaderHeight = 88.dp

// White section height is driven by the form's measured content. If the form needs more than
// MAX_WHITE_FRACTION of the screen, the green header collapses to an app bar (CompactHeaderHeight)
// and the form takes the remainder; otherwise the green section fills whatever the form doesn't
// need so its size tracks the content.
private const val MAX_WHITE_FRACTION = 0.75f
private val WhiteSectionVerticalBuffer = 24.dp

enum class AuthPanel { SignIn, SignUp, ForgotPassword }

/**
 * Unified auth screen that hosts the sign-in, sign-up and forgot-password panels. Switching
 * between panels animates the green header between the tall hero section and a compact app bar,
 * while the email field stays in place and the remaining fields animate in or out per panel.
 */
@Composable
fun SignInScreenUi(
    modifier: Modifier = Modifier,
    initialPanel: AuthPanel = AuthPanel.SignIn,
    onSkip: () -> Unit = {},
    onLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onForgotPassword: (email: String) -> Unit = {},
    onSignUp: (SignUpFormData) -> Unit = {},
) {
    var panel by remember { mutableStateOf(initialPanel) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var registrationNumber by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var batch by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val density = LocalDensity.current

    // On iOS the screen is drawn edge-to-edge (see [SignInScreenContent]) so the green and white
    // sections can color the status bar and home indicator areas respectively. These insets let us
    // size each section to include its safe area while still keeping the inner interactive content
    // padded out of the system bars.
    val statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val totalHeight = maxHeight
        // Seed with a sensible fraction of the screen so the first paint looks balanced; the real
        // measurement arrives on the next layout pass and animates from there.
        var formContentHeight by remember(totalHeight) {
            mutableStateOf(totalHeight * 0.42f)
        }

        val preferredWhite = formContentHeight + WhiteSectionVerticalBuffer + navigationBarInset
        val compactGreenHeight = CompactHeaderHeight + statusBarInset
        val targetGreenHeight = if (preferredWhite > totalHeight * MAX_WHITE_FRACTION) {
            compactGreenHeight
        } else {
            totalHeight - preferredWhite
        }

        val greenHeight by animateDpAsState(
            targetValue = targetGreenHeight,
            animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing),
            label = "greenSectionHeight",
        )

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

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(greenHeight)
                    .clip(RoundedCornerShape(bottomEnd = SectionCornerRadius))
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                // The green background itself extends into the status bar area, but the
                // interactive content (top bar + hero) is padded below the status bar.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                ) {
                    // Top bar (Skip / Back + title) crossfades between panels. The hero logo
                    // rendered below lives outside this Crossfade so it can have its own,
                    // section-aware reveal animation.
                    Crossfade(
                        targetState = panel,
                        animationSpec = tween(durationMillis = 300),
                        modifier = Modifier.fillMaxSize(),
                        label = "headerContent",
                    ) { currentPanel ->
                        when (currentPanel) {
                            AuthPanel.SignIn -> SignInHeaderContent(onSkip = onSkip)
                            AuthPanel.SignUp -> SignUpHeaderContent(
                                onBack = { panel = AuthPanel.SignIn },
                            )
                            AuthPanel.ForgotPassword -> ForgotPasswordHeaderContent(
                                onBack = { panel = AuthPanel.SignIn },
                            )
                        }
                    }

                    // Reveal the hero only after the green section has finished expanding and
                    // dismiss it as soon as the collapse begins. This makes the white section
                    // feel like it's sliding over a static hero rather than dragging it along.
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
                // As with the green section, the white background extends behind the home
                // indicator while the form content stays padded above it.
                Box(modifier = Modifier.navigationBarsPadding()) {
                    AuthFormContent(
                        panel = panel,
                        email = email,
                        password = password,
                        name = name,
                        studentId = studentId,
                        registrationNumber = registrationNumber,
                        faculty = faculty,
                        batch = batch,
                        rememberMe = rememberMe,
                        onEmailChange = { email = it },
                        onPasswordChange = { password = it },
                        onNameChange = { name = it },
                        onStudentIdChange = { studentId = it },
                        onRegistrationNumberChange = { registrationNumber = it },
                        onFacultyChange = { faculty = it },
                        onBatchChange = { batch = it },
                        onRememberMeToggle = { rememberMe = !rememberMe },
                        onLogin = { onLogin(email, password) },
                        onForgotPasswordTap = { panel = AuthPanel.ForgotPassword },
                        onResetPasswordSubmit = { onForgotPassword(email) },
                        onSignUp = {
                            onSignUp(
                                SignUpFormData(
                                    name = name,
                                    email = email,
                                    studentId = studentId,
                                    registrationNumber = registrationNumber,
                                    faculty = faculty,
                                    batch = batch,
                                    password = password,
                                ),
                            )
                        },
                        onSwitchToSignIn = { panel = AuthPanel.SignIn },
                        onSwitchToSignUp = { panel = AuthPanel.SignUp },
                        onContentMeasured = { heightPx ->
                            formContentHeight = with(density) { heightPx.toDp() }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SignInHeaderContent(onSkip: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Skip",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable(onClick = onSkip),
        )
    }
}

/**
 * Extracted so that only [BoxScope] is in scope at the call site. When this was inlined inside the
 * green `Box { ... }` (which itself lives inside a `Column { ... }`), Kotlin resolved the call to
 * [ColumnScope.AnimatedVisibility] because the outer column scope was still visible, and then
 * failed to supply its implicit receiver.
 */
@Composable
private fun BoxScope.SignInHeroReveal(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        // Wait for the green section to finish growing, then slowly reveal the hero.
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 220,
                delayMillis = 450,
                easing = FastOutSlowInEasing,
            ),
        ),
        // Mirror of the reveal: fade out at the same pace as the reveal runs in, so the hero
        // gently dissolves at the start of the white section's expand rather than snapping away.
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 220,
                easing = FastOutSlowInEasing,
            ),
        ),
        modifier = Modifier.align(Alignment.BottomCenter),
        label = "signInHeroReveal",
    ) {
        SignInHero()
    }
}

@Composable
private fun SignInHero() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .padding(bottom = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Welcome Back!",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SignUpHeaderContent(onBack: () -> Unit) {
    CompactAuthHeader(title = "Create Account", onBack = onBack)
}

@Composable
private fun ForgotPasswordHeaderContent(onBack: () -> Unit) {
    CompactAuthHeader(title = "Forgot Password", onBack = onBack)
}

@Composable
private fun CompactAuthHeader(title: String, onBack: () -> Unit) {
    // Sit the back-row at the top of the green section so it stays pinned regardless of how
    // tall the header grows (e.g. when the ForgotPassword form is short the green area can be
    // significantly larger than a typical app bar).
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.width(54.dp))
    }
}

@Composable
private fun AuthFormContent(
    panel: AuthPanel,
    email: String,
    password: String,
    name: String,
    studentId: String,
    registrationNumber: String,
    faculty: String,
    batch: String,
    rememberMe: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onStudentIdChange: (String) -> Unit,
    onRegistrationNumberChange: (String) -> Unit,
    onFacultyChange: (String) -> Unit,
    onBatchChange: (String) -> Unit,
    onRememberMeToggle: () -> Unit,
    onLogin: () -> Unit,
    onForgotPasswordTap: () -> Unit,
    onResetPasswordSubmit: () -> Unit,
    onSignUp: () -> Unit,
    onSwitchToSignIn: () -> Unit,
    onSwitchToSignUp: () -> Unit,
    onContentMeasured: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val isSignIn = panel == AuthPanel.SignIn
    val isSignUp = panel == AuthPanel.SignUp
    val isForgotPassword = panel == AuthPanel.ForgotPassword
    val fieldSpacing = 18.dp

    // Outer container scrolls when content is taller than the white section; the inner Column
    // wraps its own content so `onSizeChanged` reports the actual form height, which the parent
    // uses to size the green header against the available screen space.
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size -> onContentMeasured(size.height) }
                .padding(horizontal = 28.dp)
                .padding(top = 32.dp, bottom = 24.dp),
        ) {
            AnimatedVisibility(visible = isSignUp) {
                Column {
                    AuthUnderlinedField(
                        label = "Name",
                        value = name,
                        onValueChange = onNameChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )
                    Spacer(modifier = Modifier.height(fieldSpacing))
                }
            }

            AuthUnderlinedField(
                label = "Email Address",
                value = email,
                onValueChange = onEmailChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )

            AnimatedVisibility(visible = isSignUp) {
                Column {
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    AuthUnderlinedField(
                        label = "Student Id",
                        value = studentId,
                        onValueChange = onStudentIdChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    AuthUnderlinedField(
                        label = "Registration Number",
                        value = registrationNumber,
                        onValueChange = onRegistrationNumberChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ) {
                        AuthUnderlinedField(
                            label = "Faculty",
                            value = faculty,
                            onValueChange = onFacultyChange,
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        AuthUnderlinedField(
                            label = "Batch",
                            value = batch,
                            onValueChange = onBatchChange,
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        )
                    }
                }
            }

            AnimatedVisibility(visible = !isForgotPassword) {
                Column {
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    AuthUnderlinedField(
                        label = "Password",
                        value = password,
                        onValueChange = onPasswordChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingContent = {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { passwordVisible = !passwordVisible },
                            )
                        },
                    )
                }
            }

            AnimatedVisibility(visible = isSignIn) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    RememberMeRow(
                        rememberMe = rememberMe,
                        onRememberMeToggle = onRememberMeToggle,
                        onForgotPassword = onForgotPasswordTap,
                    )
                }
            }

            AnimatedVisibility(visible = isForgotPassword) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Enter your email and we'll send you a link to reset your password.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            AnimatedContent(
                targetState = panel,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(120))
                },
                label = "primaryButton",
            ) { currentPanel ->
                val (label, onClick) = when (currentPanel) {
                    AuthPanel.SignIn -> "LOGIN" to onLogin
                    AuthPanel.SignUp -> "SIGN UP" to onSignUp
                    AuthPanel.ForgotPassword -> "RESET PASSWORD" to onResetPasswordSubmit
                }
                PrimaryAuthButton(label = label, onClick = onClick)
            }

            Spacer(modifier = Modifier.height(28.dp))
            Crossfade(
                targetState = panel,
                animationSpec = tween(250),
                label = "bottomLink",
            ) { currentPanel ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    when (currentPanel) {
                        AuthPanel.SignIn -> AuthBottomLink(
                            prefix = "Don't have an account? ",
                            action = "SIGN UP",
                            onAction = onSwitchToSignUp,
                        )
                        AuthPanel.SignUp -> AuthBottomLink(
                            prefix = "Already have an account? ",
                            action = "LOG IN",
                            onAction = onSwitchToSignIn,
                        )
                        AuthPanel.ForgotPassword -> AuthBottomLink(
                            prefix = "Remember your password? ",
                            action = "LOG IN",
                            onAction = onSwitchToSignIn,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthBottomLink(
    prefix: String,
    action: String,
    onAction: () -> Unit,
) {
    Text(
        text = prefix,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 14.sp,
    )
    Text(
        text = action,
        color = MaterialTheme.colorScheme.tertiary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable(onClick = onAction),
    )
}

@Composable
private fun RememberMeRow(
    rememberMe: Boolean,
    onRememberMeToggle: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onRememberMeToggle),
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(if (rememberMe) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface)
                    .border(
                        width = if (rememberMe) 0.dp else 1.5.dp,
                        color = if (rememberMe) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(5.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (rememberMe) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Remember me",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
            )
        }
        Text(
            text = "Forgot Password?",
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onForgotPassword),
        )
    }
}

@Composable
private fun PrimaryAuthButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(27.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 2.sp,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun AuthUnderlinedField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val underlineColor = if (focused) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }
    val iconTint = if (focused) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                interactionSource = interactionSource,
            )
            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingContent()
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (focused) 2.dp else 1.dp)
                .background(underlineColor),
        )
    }
}

@Preview
@Composable
private fun SignInScreenUiPreview() {
    AppTheme {
        SignInScreenUi()
    }
}

@Preview
@Composable
private fun SignInScreenUiDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        SignInScreenUi()
    }
}

@Preview
@Composable
private fun SignUpPanelPreview() {
    AppTheme {
        SignInScreenUi(initialPanel = AuthPanel.SignUp)
    }
}

@Preview
@Composable
private fun SignUpPanelDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        SignInScreenUi(initialPanel = AuthPanel.SignUp)
    }
}

@Preview
@Composable
private fun ForgotPasswordPanelPreview() {
    AppTheme {
        SignInScreenUi(initialPanel = AuthPanel.ForgotPassword)
    }
}

@Preview
@Composable
private fun ForgotPasswordPanelDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        SignInScreenUi(initialPanel = AuthPanel.ForgotPassword)
    }
}
