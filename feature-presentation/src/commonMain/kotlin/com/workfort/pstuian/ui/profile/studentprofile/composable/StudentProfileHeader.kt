package com.workfort.pstuian.ui.profile.studentprofile.composable

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.profile.studentprofile.state.StudentProfileUiEvent
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_edit
import pstuian.feature_presentation.generated.resources.txt_follow
import pstuian.feature_presentation.generated.resources.txt_message

// Online presence color — distinct green that works on both light and dark
private val OnlineGreen = Color(0xFF3DB56B)

@Composable
internal fun StudentProfileHeader(
    profile: StudentProfile,
    isSignedIn: Boolean,
    selectedTabIndex: Int,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    val followLabel = stringResource(Res.string.txt_follow)
    val messageLabel = stringResource(Res.string.txt_message)
    val editLabel = stringResource(Res.string.txt_edit)

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
            AvatarWithOnlineBadge(
                imageUrl = profile.student.imageUrl,
                onClickImage = if (!profile.student.imageUrl.isNullOrEmpty()) {
                    { onUiEvent(StudentProfileUiEvent.ImageClicked(profile.student.imageUrl.orEmpty())) }
                } else {
                    null
                },
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.student.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isSignedIn) {
                        OutlinedButton(
                            onClick = { onUiEvent(StudentProfileUiEvent.EditClicked(selectedTabIndex)) },
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                            modifier = Modifier.height(34.dp),
                        ) {
                            Text(editLabel, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    } else {
                        Button(
                            onClick = { onUiEvent(StudentProfileUiEvent.FollowClicked) },
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                            modifier = Modifier.height(34.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                        ) {
                            Text(followLabel, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                        OutlinedButton(
                            onClick = { onUiEvent(StudentProfileUiEvent.EmailClicked) },
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                            modifier = Modifier.height(34.dp),
                        ) {
                            Text(messageLabel, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ── Faculty and batch info rows ───────────────────────────────────
        ProfileInfoRow(
            icon = Icons.Filled.LocationOn,
            text = profile.faculty.title,
        )
        Spacer(modifier = Modifier.height(7.dp))
        ProfileInfoRow(
            icon = Icons.Filled.DateRange,
            text = "${profile.batch.name}  ·  ${profile.student.session}",
        )

        // ── Bio ───────────────────────────────────────────────────────────
        profile.student.bio?.let { bio ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = bio,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

    }
}

@Composable
private fun AvatarWithOnlineBadge(
    imageUrl: String?,
    onClickImage: (() -> Unit)?,
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .border(2.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .padding(3.dp),
        ) {
            LoadAsyncUserImage(
                modifier = if (onClickImage != null) Modifier.clickable { onClickImage() } else Modifier,
                url = imageUrl,
                size = 72.dp,
            )
        }
        // Online badge — green circle with a surface-colored ring to separate from avatar
        Box(
            modifier = Modifier
                .size(17.dp)
                .border(2.5.dp, MaterialTheme.colorScheme.background, CircleShape)
                .background(OnlineGreen, CircleShape),
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
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
