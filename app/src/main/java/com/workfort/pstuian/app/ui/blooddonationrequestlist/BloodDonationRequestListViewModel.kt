package com.workfort.pstuian.app.ui.blooddonationrequestlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.BloodDonationRequestEntity
import com.workfort.pstuian.repository.BloodDonationRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BloodDonationRequestListViewModel(
    private val donationRequestRepo: BloodDonationRequestRepository,
    private val reducer: BloodDonationRequestListScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<BloodDonationRequestListScreenState> get() = _screenState

    private fun updateScreenState(update: BloodDonationRequestListScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() {
        updateScreenState(BloodDonationRequestListScreenStateUpdate.MessageConsumed)
    }

    fun navigationConsumed() {
        updateScreenState(BloodDonationRequestListScreenStateUpdate.NavigationConsumed)
    }

    fun onClickBack() = updateScreenState(
        BloodDonationRequestListScreenStateUpdate.NavigateTo(
            BloodDonationRequestListScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickCreateRequest() = updateScreenState(
        BloodDonationRequestListScreenStateUpdate.NavigateTo(
            BloodDonationRequestListScreenState.NavigationState.BloodDonationRequestCreateScreen,
        ),
    )

    fun onClickItem(item: BloodDonationRequestEntity) = updateScreenState(
        BloodDonationRequestListScreenStateUpdate.UpdateMessageState(
            BloodDonationRequestListScreenState.DisplayState.MessageState.ShowDetails(item),
        ),
    )

    fun onClickCall(phoneNumber: String) = updateScreenState(
        BloodDonationRequestListScreenStateUpdate.UpdateMessageState(
            BloodDonationRequestListScreenState.DisplayState.MessageState.Call(phoneNumber),
        ),
    )

    private fun isDonationRequestListLoading(): Boolean {
        val state = _screenState.value.displayState.requestListState
        return if (state is BloodDonationRequestListScreenState.DisplayState.BloodDonationRequestListState.Available) {
            state.isLoading
        } else {
            false
        }
    }

    private var requestListPage = 0
    private var endOfRequestListData = false
    private val requestListCache = arrayListOf<BloodDonationRequestEntity>()
    fun loadDonationRequests(refresh: Boolean) {
        if (isDonationRequestListLoading() || (refresh.not() && endOfRequestListData)) {
            return
        }
        if (refresh) {
            requestListPage = 0
            endOfRequestListData = false
            requestListCache.clear()
        }
        requestListPage += 1
        viewModelScope.launch {
            updateScreenState(
                BloodDonationRequestListScreenStateUpdate.ShowBloodDonationRequestList(
                    requestList = requestListCache,
                    isLoading = true,
                ),
            )
            runCatching {
                val list = donationRequestRepo.getAll(requestListPage)
                if (list.isEmpty()) {
                    endOfRequestListData = true
                } else {
                    requestListCache.addAll(list)
                }
                updateScreenState(
                    BloodDonationRequestListScreenStateUpdate.ShowBloodDonationRequestList(
                        requestList = requestListCache,
                        isLoading = false,
                    ),
                )
            }.onFailure {
                endOfRequestListData = true
                val state = if (requestListCache.isEmpty()) {
                    val message = it.message ?: "Failed to get data"
                    BloodDonationRequestListScreenStateUpdate.DataLoadFailed(message)
                } else {
                    BloodDonationRequestListScreenStateUpdate.ShowBloodDonationRequestList(
                        requestList = requestListCache,
                        isLoading = false,
                    )
                }
                updateScreenState(state)
            }
        }
    }
}