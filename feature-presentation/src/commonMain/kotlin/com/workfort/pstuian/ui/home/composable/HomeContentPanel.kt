package com.workfort.pstuian.ui.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.ErrorText
import com.workfort.pstuian.common.composable.FacultyView
import com.workfort.pstuian.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.common.composable.ShimmerBox
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.SliderView
import com.workfort.pstuian.common.composable.TitleText
import com.workfort.pstuian.common.composable.TitleTextMedium
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.ui.home.Action
import com.workfort.pstuian.ui.home.ActionItem
import com.workfort.pstuian.ui.home.HomeUiEvent
import com.workfort.pstuian.ui.home.informationItems
import com.workfort.pstuian.ui.home.optionsItems
import com.workfort.pstuian.ui.home.state.HomeUiState
import com.workfort.pstuian.ui.home.state.MessageState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.app_name
import pstuian.feature_presentation.generated.resources.label_faculties
import pstuian.feature_presentation.generated.resources.label_information_corner
import pstuian.feature_presentation.generated.resources.label_options
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_sign_in_required
import pstuian.feature_presentation.generated.resources.msg_sign_in_required
import pstuian.feature_presentation.generated.resources.txt_notification
import pstuian.feature_presentation.generated.resources.msg_request_notification_permission
import pstuian.feature_presentation.generated.resources.txt_allow
import pstuian.feature_presentation.generated.resources.label_are_you_sure
import pstuian.feature_presentation.generated.resources.data_clear_message

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeContentPanel(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    messageState: MessageState?,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    if (uiState !is HomeUiState.Content) return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TitleText(text = stringResource(Res.string.app_name))
            Spacer(modifier = Modifier.weight(1f))
            // load signed in user
            ProfileView(uiState.profileState, onUiEvent)
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = "Notification",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onUiEvent(HomeUiEvent.OnClickNotification)
                    },
            )
        }
        // slider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .layout { measurable, constraints ->
                    // removing the horizontal padding set by parent
                    val horizontalPadding = 16.dp.roundToPx()
                    // measure the composable with the padding*2 (left+right)
                    val placeable = measurable.measure(
                        constraints.offset(horizontal = horizontalPadding * 2),
                    )
                    // reset the width by removing the padding*2
                    layout(placeable.width - horizontalPadding * 2, placeable.height) {
                        // place the composable
                        placeable.place(-horizontalPadding, 0)
                    }
                }
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            SliderViewWrapper(uiState.sliderState, onUiEvent)
        }
        // faculties
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            TitleTextMedium(
                text = stringResource(Res.string.label_faculties),
                color = Color.Black,
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            FacultyViewWrapper(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(vertical = 8.dp),
                state = uiState.facultyState,
                onUiEvent = onUiEvent,
            )
        }
        // information corner
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) {
            TitleTextMedium(
                text = stringResource(Res.string.label_information_corner),
                color = Color.Black,
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            informationItems.forEach { item ->
                InformationCornerView(
                    item = item,
                    modifier = Modifier
                        .fillMaxWidth(0.48f)
                        .height(84.dp)
                        .padding(vertical = 8.dp)
                        .clickable {
                            onUiEvent(HomeUiEvent.OnClickActionItem(item))
                        },
                )
            }
        }
        // options
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) {
            TitleTextMedium(
                text = stringResource(Res.string.label_options),
                color = Color.Black,
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            optionsItems.forEach { item ->
                OptionView(
                    item = item,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(84.dp)
                        .padding(vertical = 8.dp)
                        .clickable {
                            onUiEvent(HomeUiEvent.OnClickActionItem(item))
                        },
                )
            }
        }
    }

    messageState?.let {
        HandleMessageState(it, onUiEvent)
    }
}

@Composable
private fun ProfileView(state: HomeUiState.ProfileState, onUiEvent: (HomeUiEvent) -> Unit) {
    when (state) {
        is HomeUiState.ProfileState.None -> Unit
        is HomeUiState.ProfileState.Loading -> Unit
        is HomeUiState.ProfileState.Available -> {
            val imageUrl = when(val u = state.user) {
                is StudentEntity -> u.imageUrl
                is TeacherEntity -> u.imageUrl
                else -> null
            }
            LoadAsyncUserImage(
                modifier = Modifier.clickable {
                    onUiEvent(HomeUiEvent.OnClickUserProfile)
                },
                url = imageUrl,
                size = 24.dp,
            )
        }
        is HomeUiState.ProfileState.Error -> {
            TextButton(onClick = { onUiEvent(HomeUiEvent.OnClickSignIn) }) {
                Text(text = stringResource(Res.string.txt_sign_in))
            }
        }
    }
}

@Composable
private fun SliderViewWrapper(
    state: HomeUiState.SliderState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    when (state) {
        is HomeUiState.SliderState.None -> Unit
        is HomeUiState.SliderState.Loading -> {
            ShimmerBox()
        }
        is HomeUiState.SliderState.Available -> {
            SliderView(
                sliders = state.sliders,
                scrollPosition = state.scrollPosition,
                onScrollSlider = {
                    onUiEvent(HomeUiEvent.OnScrollSlider(it))
                },
                onClickSlider = {
                    onUiEvent(HomeUiEvent.OnClickSlider(it))
                },
            )
        }
        is HomeUiState.SliderState.Error -> {
            AnimatedErrorView(modifier = Modifier.width(150.dp))
        }
    }
}

@Composable
private fun FacultyViewWrapper(
    modifier: Modifier,
    state: HomeUiState.FacultyState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    when (state) {
        is HomeUiState.FacultyState.None -> Unit
        is HomeUiState.FacultyState.Loading -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Loading, please wait...")
            }
        }
        is HomeUiState.FacultyState.Available -> {
            state.faculties.forEach { item ->
                FacultyView(
                    modifier = modifier
                        .clickable {
                            onUiEvent(HomeUiEvent.OnClickFaculty(item))
                        },
                    faculty = item,
                )
            }
        }
        is HomeUiState.FacultyState.Error -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                ErrorText(text = state.message)
            }
        }
    }
}

@Composable
private fun InformationCornerView(item: ActionItem, modifier: Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(item.title),
                modifier = Modifier
                    .weight(0.6f)
                    .padding(start = 8.dp),
            )
            val icon = item.icon
            if (icon is DrawableResource) {
                Image(
                    painterResource(icon),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f),
                )
            } else if (icon is ImageVector) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f)
                        .padding(8.dp),
                )
            }
        }
    }
}

@Composable
private fun OptionView(item: ActionItem, modifier: Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                text = stringResource(item.title),
            )
            val icon = item.icon
            if (icon is DrawableResource) {
                Image(
                    painterResource(icon),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .weight(0.3f),
                )
            } else if (icon is ImageVector) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .weight(0.3f),
                )
            }
        }
    }
}

@Composable
private fun HandleMessageState(messageState: MessageState, onUiEvent: (HomeUiEvent) -> Unit) {
    when (messageState) {
        is MessageState.SignInNecessary -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_sign_in_required),
                message = stringResource(Res.string.msg_sign_in_required),
                confirmButtonText = stringResource(Res.string.txt_sign_in),
                onConfirm = {
                    onUiEvent(HomeUiEvent.OnSignIn)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) },
            )
        }
        is MessageState.NotificationPermission -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Notifications,
                title = stringResource(Res.string.txt_notification),
                message = stringResource(Res.string.msg_request_notification_permission),
                confirmButtonText = stringResource(Res.string.txt_allow),
                onConfirm = {
                    onUiEvent(HomeUiEvent.OnRequestNotificationPermission)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) },
            )
        }
        is MessageState.ClearAllData -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.label_are_you_sure),
                message = stringResource(Res.string.data_clear_message),
                onConfirm = {
                    onUiEvent(HomeUiEvent.OnClearData)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) },
            )
        }
        is MessageState.ClearAllDataFailed -> {
            ShowErrorDialog(
                message = messageState.error,
                onConfirm = {
                    onUiEvent(HomeUiEvent.OnClearData)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) }
            )
        }
    }
}
