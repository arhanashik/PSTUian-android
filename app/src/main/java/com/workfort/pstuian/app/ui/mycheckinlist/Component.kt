package com.workfort.pstuian.app.ui.mycheckinlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.DotView
import com.workfort.pstuian.app.ui.common.component.LoadAsyncImage
import com.workfort.pstuian.app.ui.common.component.MaterialButtonToggleGroup
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInPrivacy
import com.workfort.pstuian.util.DateUtil
import com.workfort.pstuian.util.helper.MathUtil
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCheckInItemBottomSheet(
    item: CheckInEntity,
    onClickChangePrivacy: (CheckInPrivacy) -> Unit,
    onClickDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val detailsDialogSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val date = DateUtil.getTimeAgo(item.date)
    val checkInCountStr = "${MathUtil.prettyCount(item.count)} check in"
    val privacyItems = listOf(
        context.getString(R.string.txt_public),
        context.getString(R.string.txt_only_me),
    )
    val privacyIndex = when(CheckInPrivacy.create(item.privacy)) {
        null, CheckInPrivacy.PUBLIC -> 0
        CheckInPrivacy.ONLY_ME -> 1
    }
    val (selectedPrivacyIndex, onChangePrivacyIndex) = remember { mutableIntStateOf(privacyIndex) }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = detailsDialogSheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoadAsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                url = item.locationImageUrl,
                placeholder = R.drawable.ic_location_placeholder,
                contentScale = ContentScale.Crop,
            )
            TitleTextSmall(modifier = Modifier.padding(top = 8.dp), text = item.name)
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = date)
                DotView(modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)
                Text(text = checkInCountStr)
            }
            MaterialButtonToggleGroup(
                items = privacyItems,
                selectedIndex = selectedPrivacyIndex,
            ) {
                onChangePrivacyIndex(it)
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                color = Color.LightGray.copy(alpha = 0.5f),
            )
            TextButton(
                onClick = {
                    scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
                        val privacy = when (selectedPrivacyIndex) {
                            0 -> CheckInPrivacy.PUBLIC
                            1 -> CheckInPrivacy.ONLY_ME
                            else -> CheckInPrivacy.PUBLIC
                        }
                        onClickChangePrivacy(privacy)
                    }
                },
                enabled = privacyIndex != selectedPrivacyIndex,
            ) {
                Text(text = context.getString(R.string.txt_change_privacy))
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
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