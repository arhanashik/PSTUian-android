package com.workfort.pstuian.app.ui.mycheckinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInPrivacy
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.CheckInRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyCheckInListViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val checkInRepo : CheckInRepository,
    private val reducer: MyCheckInListScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<MyCheckInListScreenState> get() = _screenState

    private fun updateScreenState(update: MyCheckInListScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(MyCheckInListScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(MyCheckInListScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        MyCheckInListScreenStateUpdate.NavigateTo(MyCheckInListScreenState.NavigationState.GoBack),
    )

    fun onClickItem(item: CheckInEntity) {
        updateScreenState(
            MyCheckInListScreenStateUpdate.UpdateMessageState(
                MyCheckInListScreenState.DisplayState.MessageState.ShowDetails(item),
            ),
        )
    }

    fun onClickChangePrivacy(item: CheckInEntity, privacy: CheckInPrivacy) {
        updateScreenState(
            MyCheckInListScreenStateUpdate.UpdateMessageState(
                MyCheckInListScreenState.DisplayState.MessageState
                    .ConfirmPrivacyChange(item, privacy),
            ),
        )
    }

    fun onClickDelete(item: CheckInEntity) {
        updateScreenState(
            MyCheckInListScreenStateUpdate.UpdateMessageState(
                MyCheckInListScreenState.DisplayState.MessageState.ConfirmDelete(item),
            ),
        )
    }

    private fun isListLoading(): Boolean {
        val state = _screenState.value.displayState.checkInListState
        return if (state is MyCheckInListScreenState.DisplayState.CheckInListState.Available) {
            state.isLoading
        } else {
            false
        }
    }

    private var page = 0
    private var endOfData: Boolean = false
    private val itemsCache = arrayListOf<CheckInEntity>()
    fun loadCheckInList(refresh: Boolean) {
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
            MyCheckInListScreenStateUpdate.ShowDataList(
                items = itemsCache,
                isLoading = true,
            ),
        )
        viewModelScope.launch {
            runCatching {
                val list = checkInRepo.getAll(
                    userId = userId,
                    userType = userType.type,
                    page = page,
                )
                if (list.isEmpty()) {
                    endOfData = true
                } else {
                    itemsCache.addAll(list)
                }
                updateScreenState(
                    MyCheckInListScreenStateUpdate.ShowDataList(
                        items = itemsCache,
                        isLoading = false,
                    ),
                )
            }.onFailure {
                endOfData = true
                if (itemsCache.isEmpty()) {
                    val message = it.message ?: "Failed to load data"
                    updateScreenState(
                        MyCheckInListScreenStateUpdate.DataLoadFailed(message),
                    )
                } else {
                    updateScreenState(
                        MyCheckInListScreenStateUpdate.ShowDataList(
                            items = itemsCache,
                            isLoading = false,
                        ),
                    )
                }
            }
        }
    }

    fun changePrivacy(item: CheckInEntity, privacy: CheckInPrivacy) {
        viewModelScope.launch {
            updateScreenState(
                MyCheckInListScreenStateUpdate.UpdateMessageState(
                    MyCheckInListScreenState.DisplayState.MessageState.Loading(cancelable = false),
                ),
            )
            runCatching {
                checkInRepo.updatePrivacy(item.id, privacy.value)
            }.onSuccess {
                updateScreenState(
                    MyCheckInListScreenStateUpdate.UpdateMessageState(
                        MyCheckInListScreenState.DisplayState.MessageState
                            .Success("Changed successfully"),
                    ),
                )
                loadCheckInList(refresh = true)
            }.onFailure {
                val message = it.message ?: "Failed to change. Please try again."
                updateScreenState(
                    MyCheckInListScreenStateUpdate.UpdateMessageState(
                        MyCheckInListScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    fun delete(item: CheckInEntity) {
        viewModelScope.launch {
            updateScreenState(
                MyCheckInListScreenStateUpdate.UpdateMessageState(
                    MyCheckInListScreenState.DisplayState.MessageState.Loading(cancelable = false),
                ),
            )
            runCatching {
                checkInRepo.delete(item.id)
            }.onSuccess {
                updateScreenState(
                    MyCheckInListScreenStateUpdate.UpdateMessageState(
                        MyCheckInListScreenState.DisplayState.MessageState.Success(
                            message = "Deleted successfully",
                        ),
                    ),
                )
                loadCheckInList(refresh = true)
            }.onFailure {
                val message = it.message ?: "Failed to delete. Please try again"
                updateScreenState(
                    MyCheckInListScreenStateUpdate.UpdateMessageState(
                        MyCheckInListScreenState.DisplayState.MessageState.Success(message),
                    ),
                )
            }
        }
    }
}