package com.workfort.pstuian.app.ui.myblooddonationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.BloodDonationEntity
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.BloodDonationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyBloodDonationListViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val donationRepo: BloodDonationRepository,
    private val reducer: MyBloodDonationListScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<MyBloodDonationListScreenState> get() = _screenState

    private fun updateScreenState(update: MyBloodDonationListScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() {
        updateScreenState(MyBloodDonationListScreenStateUpdate.MessageConsumed)
    }

    fun navigationConsumed() {
        updateScreenState(MyBloodDonationListScreenStateUpdate.NavigationConsumed)
    }

    fun onClickBack() = updateScreenState(
        MyBloodDonationListScreenStateUpdate.NavigateTo(
            MyBloodDonationListScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickCreateRequest() = updateScreenState(
        MyBloodDonationListScreenStateUpdate.NavigateTo(
            MyBloodDonationListScreenState.NavigationState.BloodDonationRequestCreateScreen,
        ),
    )

    fun onClickEdit(item: BloodDonationEntity) = updateScreenState(
        MyBloodDonationListScreenStateUpdate.NavigateTo(
            MyBloodDonationListScreenState.NavigationState.BloodDonationRequestEditScreen(item),
        ),
    )

    fun onClickDelete(item: BloodDonationEntity) = updateScreenState(
        MyBloodDonationListScreenStateUpdate.UpdateMessageState(
            MyBloodDonationListScreenState.DisplayState.MessageState.ConfirmDelete(item),
        ),
    )

    private fun isListLoading(): Boolean {
        val state = _screenState.value.displayState.listState
        return if (state is MyBloodDonationListScreenState.DisplayState.BloodDonationListState.Available) {
            state.isLoading
        } else {
            false
        }
    }

    private var page = 0
    private var endOfData = false
    private val listCache = arrayListOf<BloodDonationEntity>()
    fun loadDonationList(refresh: Boolean) {
        if (isListLoading() || (refresh.not() && endOfData)) {
            return
        }
        if (refresh) {
            page = 0
            endOfData = false
            listCache.clear()
        }
        page += 1
        viewModelScope.launch {
            updateScreenState(
                MyBloodDonationListScreenStateUpdate.ShowMyBloodDonationList(
                    items = listCache,
                    isLoading = true,
                ),
            )
            runCatching {
                val list = donationRepo.getAll(userId, userType.type, page)
                if (list.isEmpty()) {
                    endOfData = true
                } else {
                    listCache.addAll(list)
                }
                updateScreenState(
                    MyBloodDonationListScreenStateUpdate.ShowMyBloodDonationList(
                        items = listCache,
                        isLoading = false,
                    ),
                )
            }.onFailure {
                endOfData = true
                val state = if (listCache.isEmpty()) {
                    val message = it.message ?: "Failed to get data"
                    MyBloodDonationListScreenStateUpdate.DataLoadFailed(message)
                } else {
                    MyBloodDonationListScreenStateUpdate.ShowMyBloodDonationList(
                        items = listCache,
                        isLoading = false,
                    )
                }
                updateScreenState(state)
            }
        }
    }

    fun deleteDonation(item: BloodDonationEntity) {
        updateScreenState(
            MyBloodDonationListScreenStateUpdate.UpdateMessageState(
                MyBloodDonationListScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                donationRepo.delete(item.id)
                updateScreenState(
                    MyBloodDonationListScreenStateUpdate.UpdateMessageState(
                        MyBloodDonationListScreenState.DisplayState.MessageState
                            .Success("Deleted successfully"),
                    ),
                )
                loadDonationList(refresh = true)
            }.onFailure {
                val message = it.message ?: "Failed to delete"
                updateScreenState(
                    MyBloodDonationListScreenStateUpdate.UpdateMessageState(
                        MyBloodDonationListScreenState.DisplayState.MessageState.Failure(message),
                    ),
                )
            }
        }
    }
}