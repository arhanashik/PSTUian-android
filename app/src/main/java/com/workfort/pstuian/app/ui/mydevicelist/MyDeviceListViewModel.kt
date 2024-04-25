package com.workfort.pstuian.app.ui.mydevicelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyDeviceListViewModel(
    private val authRepo: AuthRepository,
    private val reducer: MyDeviceListScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<MyDeviceListScreenState> get() = _screenState

    private fun updateScreenState(update: MyDeviceListScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(MyDeviceListScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(MyDeviceListScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        MyDeviceListScreenStateUpdate.NavigateTo(
            MyDeviceListScreenState.NavigationState.GoBack(isSignedOutFromAll = false),
        ),
    )

    fun onClickItem(item: DeviceEntity) {
        updateScreenState(
            MyDeviceListScreenStateUpdate.UpdateMessageState(
                MyDeviceListScreenState.DisplayState.MessageState.ShowDetails(item),
            ),
        )
    }

    fun onClickDelete(item: DeviceEntity) {
        messageConsumed()
    }

    fun onClickSignOutFromAllDevice() {
        val state = if (itemsCache.isEmpty()) {
            MyDeviceListScreenStateUpdate.UpdateMessageState(
                MyDeviceListScreenState.DisplayState.MessageState.Error("No device to sign out"),
            )
        } else {
            MyDeviceListScreenStateUpdate.UpdateMessageState(
                MyDeviceListScreenState.DisplayState.MessageState.ConfirmSignOutFromAll,
            )
        }
        updateScreenState(state)
    }

    private fun isListLoading(): Boolean {
        val state = _screenState.value.displayState.listState
        return if (state is MyDeviceListScreenState.DisplayState.DeviceListState.Available) {
            state.isLoading
        } else {
            false
        }
    }

    private var page = 0
    private var endOfData: Boolean = false
    private val itemsCache = arrayListOf<DeviceEntity>()
    fun loadDeviceList(refresh: Boolean) {
        if (isListLoading() || (refresh.not() && endOfData)) {
            return
        }
        if (refresh) {
            page = 0
            endOfData = false
            itemsCache.clear()
        }
        page += 1
        updateScreenState(
            MyDeviceListScreenStateUpdate.ShowDataList(
                items = itemsCache,
                isLoading = true,
            ),
        )
        viewModelScope.launch {
            runCatching {
                val list = authRepo.getAllDevices(page)
                if (list.isEmpty()) {
                    endOfData = true
                } else {
                    itemsCache.addAll(list)
                }
                updateScreenState(
                    MyDeviceListScreenStateUpdate.ShowDataList(
                        items = itemsCache,
                        isLoading = false,
                    ),
                )
            }.onFailure {
                endOfData = true
                if (itemsCache.isEmpty()) {
                    val message = it.message ?: "Failed to load data"
                    updateScreenState(
                        MyDeviceListScreenStateUpdate.DataLoadFailed(message),
                    )
                } else {
                    updateScreenState(
                        MyDeviceListScreenStateUpdate.ShowDataList(
                            items = itemsCache,
                            isLoading = false,
                        ),
                    )
                }
            }
        }
    }

    fun signOutFromAllDevices() {
        updateScreenState(
            MyDeviceListScreenStateUpdate.UpdateMessageState(
                MyDeviceListScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                authRepo.signOut(fromAllDevice = true)
            }.onSuccess {
                messageConsumed()
                updateScreenState(
                    MyDeviceListScreenStateUpdate.NavigateTo(
                        MyDeviceListScreenState.NavigationState.GoBack(isSignedOutFromAll = true),
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Failed to sign out from all devices. Please try again."
                messageConsumed()
                updateScreenState(
                    MyDeviceListScreenStateUpdate.UpdateMessageState(
                        MyDeviceListScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }
}
