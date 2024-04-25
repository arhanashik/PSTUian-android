package com.workfort.pstuian.app.ui.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.R
import com.workfort.pstuian.model.ProfileInfoItem
import com.workfort.pstuian.model.ProfileInfoItemAction

@Composable
fun List<ProfileInfoItem>.ProfileInfoListView(
    modifier: Modifier = Modifier,
    onClickAction: (ProfileInfoItem) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@ProfileInfoListView) { infoItem ->
            ProfileInfoListItemView(
                modifier = if (infoItem.action is ProfileInfoItemAction.None) {
                    Modifier
                } else {
                    Modifier.clickable {
                        if (infoItem.action !is ProfileInfoItemAction.None) {
                            onClickAction(infoItem)
                        }
                    }
                },
                infoItem = infoItem,
            )
        }
    }
}

@Composable
fun ProfileInfoListItemView(
    modifier: Modifier,
    infoItem: ProfileInfoItem,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            LabelText(text = infoItem.label)
            Text(text = infoItem.title.ifEmpty { "~" })
        }
        Spacer(modifier = Modifier.weight(1f))
        infoItem.action.getIcon()?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = "Profile Info Item Action Icon"
            )
        }
    }
}

@Composable
private fun ProfileInfoItemAction.getIcon(): ImageVector? = when (this) {
    is ProfileInfoItemAction.None -> null
    is ProfileInfoItemAction.Edit -> Icons.Default.Edit
    is ProfileInfoItemAction.Call -> Icons.Default.Call
    is ProfileInfoItemAction.Email -> Icons.Default.Email
    is ProfileInfoItemAction.DownloadCv -> null
    is ProfileInfoItemAction.Link -> null
    is ProfileInfoItemAction.Password -> Icons.Default.Lock
    is ProfileInfoItemAction.UploadCv -> ImageVector.vectorResource(id = R.drawable.ic_arrow_up)
    is ProfileInfoItemAction.BloodDonationList -> null
    is ProfileInfoItemAction.CheckInList -> Icons.Default.LocationOn
    is ProfileInfoItemAction.SignedInDevices -> null
    is ProfileInfoItemAction.DeleteAccount -> Icons.Default.Delete
}