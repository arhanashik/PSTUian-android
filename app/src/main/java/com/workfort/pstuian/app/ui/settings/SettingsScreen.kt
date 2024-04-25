package com.workfort.pstuian.app.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowInfoDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.util.helper.LinkUtil


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<SettingsScreenUiEvent>(SettingsScreenUiEvent.LoadInitialData)
    }
    val linkUtil = LinkUtil(context)

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(navController) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            is SettingsScreenUiEvent.None -> Unit
            is SettingsScreenUiEvent.LoadInitialData -> viewModel.loadInitialData()
            is SettingsScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is SettingsScreenUiEvent.OnClickContactUs ->
                linkUtil.sendEmail(context.getString(R.string.dev_team_email))
            is SettingsScreenUiEvent.OnChangeShowNotification -> viewModel.setShowNotification(show)
            is SettingsScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is SettingsScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = SettingsScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: SettingsScreenState.DisplayState,
    onUiEvent: (SettingsScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.label_settings_screen),
                onClickBack = { onUiEvent(SettingsScreenUiEvent.OnClickBack) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            NotificationSettingsView(displayState.showNotification) {
                onUiEvent(SettingsScreenUiEvent.OnChangeShowNotification(it))
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            InfoPanel(onUiEvent)
        }
    }
}

@Composable
private fun NotificationSettingsView(
    showNotification: Boolean,
    onSettingsChange: (Boolean) -> Unit,
) {
    ElevatedCard(shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TitleTextSmall(text = "Show Notification")
            Switch(
                checked = showNotification,
                onCheckedChange = { onSettingsChange(it) },
                thumbContent = {
                    if (showNotification) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedBorderColor = Color.LightGray,
                ),
            )
        }
    }
}

@Composable
private fun InfoPanel(onUiEvent: (SettingsScreenUiEvent) -> Unit) {
    val context = LocalContext.current

    Column {
        ElevatedCard(shape = RoundedCornerShape(16.dp)) {
            Text(
                text = context.getString(R.string.about_it),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp),
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        ElevatedCard(shape = RoundedCornerShape(16.dp)) {
            Text(
                text = context.getString(R.string.data_load_policy),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp),
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = context.getString(R.string.dev_team),
                    textAlign = TextAlign.Center,
                )
                TextButton(
                    onClick = {
                        onUiEvent(SettingsScreenUiEvent.OnClickContactUs)
                    }
                ) {
                    Text(
                        text = context.getString(R.string.label_contact_us),
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (SettingsScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun SettingsScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (SettingsScreenUiEvent) -> Unit,
) {
    when (this) {
        is SettingsScreenState.DisplayState.MessageState.Success -> {
            ShowInfoDialog(
                message = message,
                onDismiss = {
                    onUiEvent(SettingsScreenUiEvent.MessageConsumed)
                },
            )
        }
        is SettingsScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(SettingsScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(SettingsScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun SettingsScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (SettingsScreenUiEvent) -> Unit,
) {
    when (this) {
        is SettingsScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(SettingsScreenUiEvent.NavigationConsumed)
}