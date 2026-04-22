package com.workfort.pstuian.ui.signin.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle

@Composable
internal fun AuthBottomLink(
    prefix: String,
    action: String,
    onAction: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = prefix,
            style = TextStyle.label1.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = action,
            style = TextStyle.label1.copy(color = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.clickable(onClick = onAction),
        )
    }
}

@Composable
internal fun AuthPrivacyPolicyAndTermsLink(
    onTermsAndConditionsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "By signing by you'll automatically agree to the",
            style = TextStyle.label2.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Terms & Conditions",
                style = TextStyle.label1.copy(color = MaterialTheme.colorScheme.tertiary),
                modifier = Modifier.clickable(onClick = onTermsAndConditionsClick),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "and",
                style = TextStyle.label2.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Privacy Policy",
                style = TextStyle.label1.copy(color = MaterialTheme.colorScheme.tertiary),
                modifier = Modifier.clickable(onClick = onPrivacyPolicyClick),
            )
        }
    }
}

@Composable
internal fun RememberMeRow(
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
            style = TextStyle.label1.copy(color = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.clickable(onClick = onForgotPassword),
        )
    }
}

@Composable
internal fun AuthFormPanelLayout(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(top = 40.dp, bottom = 24.dp),
        content = content,
    )
}

@Composable
internal fun AuthUnderlinedField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    leadingIcon: ImageVector? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    val (underlineColor, iconTint) = if (focused) {
        MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.outlineVariant to MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle.label2.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
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
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (focusRequester != null) {
                            Modifier.focusRequester(focusRequester)
                        } else {
                            Modifier
                        },
                    ),
                textStyle = TextStyle.body1.copy(color = AppColors.textPrimary),
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
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

@Composable
internal fun AuthPasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    AuthUnderlinedField(
        label = "Password",
        value = password,
        onValueChange = onPasswordChange,
        modifier = modifier,
        focusRequester = focusRequester,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
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
