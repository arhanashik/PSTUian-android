package com.workfort.pstuian.ui.common.composable.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.WarningBox
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_sign_out
import pstuian.feature_presentation.generated.resources.msg_sign_out_from_all
import pstuian.feature_presentation.generated.resources.txt_dismiss
import pstuian.feature_presentation.generated.resources.txt_sign_out
import pstuian.feature_presentation.generated.resources.txt_sign_out_from_all

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSignOutBottomSheet(
    onConfirm: (Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var signOutFromAllDevices by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(Res.string.txt_sign_out),
                style = TextStyle.title2,
                color = AppColors.textPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(Res.string.msg_sign_out),
                style = TextStyle.body1,
                color = AppColors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { signOutFromAllDevices = !signOutFromAllDevices },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = signOutFromAllDevices,
                    onCheckedChange = { signOutFromAllDevices = it },
                )
                Text(
                    text = stringResource(Res.string.txt_sign_out_from_all),
                    style = TextStyle.body2,
                    color = AppColors.textPrimary,
                )
            }
            if (signOutFromAllDevices) {
                WarningBox(stringResource(Res.string.msg_sign_out_from_all))
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = if (signOutFromAllDevices) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                onClick = { onConfirm(signOutFromAllDevices) },
            ) {
                Text(
                    text = stringResource(Res.string.txt_sign_out),
                    style = TextStyle.label1,
                )
            }
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(Res.string.txt_dismiss),
                    style = TextStyle.body2,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
