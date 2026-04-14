package com.workfort.pstuian.ui.mycheckinlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.mycheckinlist.composable.MyCheckInListContentPanel
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiEvent
import com.workfort.pstuian.ui.mycheckinlist.state.NavigationState

@Composable
internal fun MyCheckInListScreen(
    viewModel: MyCheckInListViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val messageState by viewModel.messageState.collectAsState()
    val navigationState by viewModel.navigationState.collectAsState()

    LaunchedEffect(key1 = navigationState) {
        when (val state = navigationState) {
            null -> Unit
            is NavigationState.GoBack -> {
                viewModel.navigationConsumed()
            }
        }
    }

    MyCheckInListContentPanel(
        uiState = uiState,
        messageState = messageState,
        onUiEvent = { event ->
            when (event) {
                is MyCheckInListUiEvent.LoadMoreData -> viewModel.loadCheckInList(event.refresh)
                is MyCheckInListUiEvent.ClickBack -> viewModel.onClickBack()
                is MyCheckInListUiEvent.ClickItem -> viewModel.onClickItem(event.item)
                is MyCheckInListUiEvent.ClickChangePrivacy -> viewModel.onClickChangePrivacy(event.item, event.privacy)
                is MyCheckInListUiEvent.ClickDelete -> viewModel.onClickDelete(event.item)
                is MyCheckInListUiEvent.ChangePrivacy -> viewModel.changePrivacy(event.item, event.privacy)
                is MyCheckInListUiEvent.Delete -> viewModel.delete(event.item)
                is MyCheckInListUiEvent.MessageConsumed -> viewModel.messageConsumed()
                is MyCheckInListUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
            }
        },
    )
}
