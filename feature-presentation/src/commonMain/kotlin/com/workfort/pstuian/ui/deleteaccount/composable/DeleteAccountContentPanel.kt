package com.workfort.pstuian.ui.deleteaccount.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.OutlinedTextInput
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiEvent
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_password
import pstuian.feature_presentation.generated.resources.msg_delete_account
import pstuian.feature_presentation.generated.resources.txt_delete_account

@Composable
fun DeleteAccountContentPanel(
    uiState: DeleteAccountUiState,
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FormContent(
            input = uiState.input,
            validationError = uiState.validationError,
            onUiEvent = onUiEvent,
        )
    }
}

@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    input: String,
    validationError: String,
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {
    val (newInput, onChangeInput) = remember(input) { mutableStateOf(input) }
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(newInput) {
        onUiEvent(DeleteAccountUiEvent.OnChangeInput(newInput))
    }

    Column(modifier = modifier.padding(16.dp)) {
        TitleTextSmall(text = stringResource(Res.string.msg_delete_account))
        Spacer(modifier = Modifier.padding(top = 16.dp))
        OutlinedTextInput(
            label = stringResource(Res.string.hint_password),
            value = newInput,
            inputType = KeyboardType.Password,
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = validationError.isNotEmpty(),
            supportingText = validationError,
        ) {
            onChangeInput(it)
        }
        TextButton(
            onClick = {
                onUiEvent(DeleteAccountUiEvent.OnClickDeleteAccountBtn)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            enabled = validationError.isEmpty(),
        ) {
            Text(
                stringResource(Res.string.txt_delete_account),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}
