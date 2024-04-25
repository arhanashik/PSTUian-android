package com.workfort.pstuian.app.ui.mydevicelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.util.DateUtil
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDeviceItemBottomSheet(
    item: DeviceEntity,
    onClickDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val detailsDialogSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val lastActiveAt = DateUtil.getTimeAgo(item.updatedAt?: "")
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = detailsDialogSheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TitleTextSmall(text = item.model ?: "Unknown")
            Text(text = "IP Address: ${item.ipAddress}")
            Text(text = "Last Activity: $lastActiveAt")
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                color = Color.LightGray.copy(alpha = 0.5f),
            )
            TextButton(
                onClick = {
                    scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
                        onClickDelete()
                    }
                }
            ) {
                Text(text = context.getString(R.string.txt_delete))
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            TextButton(
                onClick = {
                    scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
                        onDismiss()
                    }
                }
            ) {
                Text(text = context.getString(R.string.txt_dismiss))
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}