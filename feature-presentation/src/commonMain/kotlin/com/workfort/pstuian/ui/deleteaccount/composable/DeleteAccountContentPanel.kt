package com.workfort.pstuian.ui.deleteaccount.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.common.composable.InfoBox
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiEvent
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiState
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedField

@Composable
fun DeleteAccountContentPanel(
    uiState: DeleteAccountUiState,
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        InfoBox("Deleting your account will result in the permanent loss of all your data. We recommend deactivating your account instead to keep your information safe. You can activate it by signing in anytime.")
        
        Spacer(modifier = Modifier.padding(top = 16.dp))
        
        FormContent(
            input = uiState.input,
            validationError = uiState.validationError,
            onUiEvent = onUiEvent,
        )
        
        Spacer(modifier = Modifier.padding(top = 16.dp))
        
        ActionButton(
            label = "Deactivate Account",
            onClick = { onUiEvent(DeleteAccountUiEvent.DeactivateAccountClicked(uiState.input)) }
        )
        
        Spacer(modifier = Modifier.padding(top = 24.dp))
        
        InfoBox("Note that deleting an account might take some time once the request is placed. In the meantime you can deactivate your account if you don't want your profile to be visible.")

        Spacer(modifier = Modifier.padding(top = 16.dp))
        
        Button(
            onClick = { onUiEvent(DeleteAccountUiEvent.DeleteAccountClicked) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text("Request to Delete Account", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FormContent(
    input: String,
    validationError: String,
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    AuthUnderlinedField(
        label = "Password",
        value = input,
        onValueChange = { onUiEvent(DeleteAccountUiEvent.OnChangeInput(it)) },
        isError = validationError.isNotEmpty(),
        supportingText = validationError.takeIf { it.isNotEmpty() },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingContent = {
            Icon(
                imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                modifier = Modifier
                    .size(20.dp)
                    .clickable { passwordVisibility = !passwordVisibility },
            )
        }
    )
}
