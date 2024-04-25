package com.workfort.pstuian.app.ui.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.workfort.pstuian.R

@Composable
fun ShowLoaderDialog(
    modifier: Modifier = Modifier,
    title: String = LocalContext.current.getString(R.string.txt_please_wait),
    cancelable: Boolean = true,
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable,
        ),
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = title)
                Spacer(modifier = Modifier.padding(top = 16.dp))
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ShowConfirmationDialog(
    icon: ImageVector? = null,
    title: String = LocalContext.current.getString(R.string.txt_confirmation),
    message: String = LocalContext.current.getString(R.string.label_are_you_sure),
    confirmButtonText: String = LocalContext.current.getString(R.string.txt_confirm),
    dismissButtonText: String? = LocalContext.current.getString(R.string.txt_dismiss),
    cancelable: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ShowAlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable,
        ),
        icon = icon,
        title = title,
        message = message,
        confirmButtonText = confirmButtonText,
        dismissButtonText = dismissButtonText,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}

@Composable
fun ShowSuccessDialog(
    icon: ImageVector = Icons.Default.CheckCircle,
    title: String = LocalContext.current.getString(R.string.default_success_dialog_title),
    message: String = LocalContext.current.getString(R.string.default_success_dialog_message),
    confirmButtonText: String = LocalContext.current.getString(R.string.txt_dismiss),
    dismissButtonText: String? = null,
    cancelable: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = { /*No-Op*/ },
) {
    ShowAlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable,
        ),
        icon = icon,
        title = title,
        message = message,
        confirmButtonText = confirmButtonText,
        dismissButtonText = dismissButtonText,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}

@Composable
fun ShowErrorDialog(
    icon: ImageVector = Icons.Default.Info,
    title: String = LocalContext.current.getString(R.string.default_error_dialog_title),
    message: String = LocalContext.current.getString(R.string.default_error_dialog_message),
    confirmButtonText: String = LocalContext.current.getString(R.string.txt_retry),
    dismissButtonText: String? = LocalContext.current.getString(R.string.txt_dismiss),
    cancelable: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ShowAlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable,
        ),
        icon = icon,
        title = title,
        message = message,
        confirmButtonText = confirmButtonText,
        dismissButtonText = dismissButtonText,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}

@Composable
fun ShowInfoDialog(
    icon: ImageVector? = null,
    title: String = LocalContext.current.getString(R.string.txt_information),
    message: String,
    dismissButtonText: String = LocalContext.current.getString(R.string.txt_dismiss),
    cancelable: Boolean = true,
    onDismiss: () -> Unit,
) {
    ShowAlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable,
        ),
        icon = icon,
        title = title,
        message = message,
        confirmButtonText = dismissButtonText,
        dismissButtonText = null,
        onConfirm = onDismiss,
        onDismiss = onDismiss,
    )
}

@Composable
fun ShowAlertDialog(
    properties: DialogProperties,
    icon: ImageVector?,
    title: String,
    message: String,
    confirmButtonText: String,
    dismissButtonText: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        icon = { icon?.let { Icon(it, contentDescription = null) } },
        title = { Text(text = title) },
        text = { Text(text = message, textAlign = TextAlign.Center) },
        properties = properties,
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onConfirm()
                },
                colors = ButtonDefaults.buttonColors(Color.Black),
            ) {
                Text(
                    confirmButtonText,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        },
        dismissButton = {
            dismissButtonText?.let {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text(it, color = Color.Black)
                }
            }
        },
    )
}

@Composable
fun ShowInputDialog(
    icon: ImageVector? = null,
    title: String,
    label: String,
    input: String = "",
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    maxLength: Int = 500,
    confirmButtonText: String,
    dismissButtonText: String? = LocalContext.current.getString(R.string.txt_dismiss),
    cancelable: Boolean = true,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val (newInput, onChangeInput) = remember { mutableStateOf(input) }
    AlertDialog(
        icon = { icon?.let { Icon(it, contentDescription = null) } },
        title = { Text(text = title) },
        text = {
            OutlinedTextInput(
                label = label,
                value = newInput,
                singleLine = singleLine,
                minLines = minLines,
                maxLines = maxLines,
                supportingText = "${newInput.length}/$maxLength",
            ) {
                if (it.length <= maxLength) {
                    onChangeInput(it)
                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable,
        ),
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(newInput)
                },
                colors = ButtonDefaults.buttonColors(Color.Black),
            ) {
                Text(
                    confirmButtonText,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        },
        dismissButton = {
            dismissButtonText?.let {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text(it, color = Color.Black)
                }
            }
        },
    )
}