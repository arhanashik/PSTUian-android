package com.workfort.pstuian.ui.profile.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.common.composable.OnlineOfflineStatusLabel
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.UserPresenceDisplayData
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_change
import pstuian.feature_presentation.generated.resources.txt_follow
import pstuian.feature_presentation.generated.resources.txt_message

@Composable
internal fun ProfileHeader(
    displayData: ProfileHeaderDisplayData,
    isSignedIn: Boolean,
    userPresence: UserPresenceDisplayData,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    var showOwnProfilePictureDialog by remember { mutableStateOf(false) }
    val followLabel = stringResource(Res.string.txt_follow)
    val messageLabel = stringResource(Res.string.txt_message)
    val editLabel = "Edit Profile"

    if (showOwnProfilePictureDialog) {
        OwnProfilePictureDialog(
            imageUrl = displayData.imageUrl,
            isOnline = userPresence.isOnline,
            changeLabel = stringResource(Res.string.txt_change),
            onDismiss = { showOwnProfilePictureDialog = false },
            onChangeClick = {
                onUiEvent(ProfileUiEvent.ChangeImageClicked)
                showOwnProfilePictureDialog = false
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        // ── Avatar + Name/Buttons row ─────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarWithStatusChip(
                imageUrl = displayData.imageUrl,
                isOnline = userPresence.isOnline,
                onAvatarClick = {
                    if (isSignedIn) {
                        showOwnProfilePictureDialog = true
                    } else {
                        displayData.imageUrl?.takeIf { it.isNotEmpty() }?.let { url ->
                            onUiEvent(ProfileUiEvent.ImageClicked(url))
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                TitleTextSmall(
                    text = displayData.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.textPrimary,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isSignedIn) {
                        OutlinedButton(
                            onClick = { onUiEvent(ProfileUiEvent.EditClicked) },
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                            modifier = Modifier.height(28.dp),
                        ) {
                            Text(editLabel, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    } else {
                        Button(
                            onClick = { onUiEvent(ProfileUiEvent.FollowClicked) },
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                            modifier = Modifier.height(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppColors.primary,
                            ),
                        ) {
                            Text(
                                text = followLabel,
                                style = TextStyle.label2,
                                fontSize = 12.sp,
                                color = AppColors.onPrimary,
                            )
                        }
                        OutlinedButton(
                            onClick = { onUiEvent(ProfileUiEvent.EmailClicked) },
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                            modifier = Modifier.height(28.dp),
                        ) {
                            Text(
                                text = messageLabel,
                                style = TextStyle.label2,
                                fontSize = 12.sp,
                                color = AppColors.textPrimary,
                            )
                        }
                    }
                }
            }
        }

        // ── Info rows ───────────────────────────────────
        if (!displayData.infoItem1.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(18.dp))
            ProfileInfoRow(icon = Icons.Filled.Book, text = displayData.infoItem1)
        }
        if (!displayData.infoItem2.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            ProfileInfoRow(icon = Icons.Filled.Info, text = displayData.infoItem2)
        }

        // ── Bio ───────────────────────────────────────────────────────────
        if (!displayData.bio.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = displayData.bio,
                style = TextStyle.body3,
                color = AppColors.textSecondary,
            )
        }
    }
}

/** Light gray for offline ring (distinct from online primary). */
private val OfflineStatusLightGray = Color(0xFFD6D6D6)

@Composable
private fun statusRingBorderColor(isOnline: Boolean): Color =
    if (isOnline) MaterialTheme.colorScheme.primary else OfflineStatusLightGray

@Composable
private fun OwnProfilePictureDialog(
    imageUrl: String?,
    isOnline: Boolean,
    changeLabel: String,
    onDismiss: () -> Unit,
    onChangeClick: () -> Unit,
) {
    val ringBorderColor = statusRingBorderColor(isOnline)
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(width = 3.dp, color = ringBorderColor, shape = CircleShape)
                        .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                        .padding(5.dp),
                ) {
                    LoadAsyncUserImage(url = imageUrl, size = 132.dp)
                }
                Button(
                    onClick = onChangeClick,
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.primary,
                    ),
                ) {
                    Text(
                        text = changeLabel,
                        style = TextStyle.label1,
                        color = AppColors.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
private fun AvatarWithStatusChip(
    imageUrl: String?,
    isOnline: Boolean,
    onAvatarClick: () -> Unit,
) {
    val ringBorderColor = statusRingBorderColor(isOnline)

    Box {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(CircleShape)
                .clickable { onAvatarClick() }
                .border(width = 2.dp, color = ringBorderColor, shape = CircleShape)
                .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                .padding(4.dp),
        ) {
            LoadAsyncUserImage(url = imageUrl, size = 64.dp)
        }
        OnlineOfflineStatusLabel(
            isOnline = isOnline,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .zIndex(1f),
        )
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppColors.primary,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = text,
            style = TextStyle.body2,
            color = AppColors.textPrimary,
        )
    }
}
