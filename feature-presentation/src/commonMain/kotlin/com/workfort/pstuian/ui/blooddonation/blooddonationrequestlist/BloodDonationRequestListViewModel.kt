package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.BloodDonationRequestRepository
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListNavigationState
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BloodDonationRequestListViewModel(
    private val donationRequestRepo: BloodDonationRequestRepository,
    private val uiStateMachine: BloodDonationRequestListUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<BloodDonationRequestListUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<BloodDonationRequestListMessageState?>(null)
    val message: StateFlow<BloodDonationRequestListMessageState?> = _message

    private val _navigation = MutableStateFlow<BloodDonationRequestListNavigationState?>(null)
    val navigation: StateFlow<BloodDonationRequestListNavigationState?> = _navigation

    private var page = 1
    private var hasMoreData = true
    private val requestListCache = arrayListOf<BloodDonationRequestEntity>()

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
        loadDonationRequests(forceRefresh = true)
    }

    fun onUiEvent(event: BloodDonationRequestListUiEvent) {
        when (event) {
            is BloodDonationRequestListUiEvent.BackClicked -> onClickBack()
            is BloodDonationRequestListUiEvent.CreateRequestClicked -> onClickCreateRequest()
            is BloodDonationRequestListUiEvent.ItemClicked -> onClickItem(event.item)
            is BloodDonationRequestListUiEvent.CallClicked -> onClickCall(event.phoneNumber)
            is BloodDonationRequestListUiEvent.LoadMore -> loadDonationRequests(forceRefresh = false)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { BloodDonationRequestListNavigationState.GoBack }
    }

    private fun onClickCreateRequest() {
        _navigation.update { BloodDonationRequestListNavigationState.BloodDonationRequestCreateScreen }
    }

    private fun onClickItem(item: BloodDonationRequestEntity) {
        _message.update { BloodDonationRequestListMessageState.ShowDetails(item) }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update { BloodDonationRequestListMessageState.Call(phoneNumber) }
    }

    private fun loadDonationRequests(forceRefresh: Boolean) {
        val currentState = uiState.value as? BloodDonationRequestListUiState.Content
        if (currentState?.isLoading == true || (forceRefresh.not() && hasMoreData)) {
            return
        }
        if (forceRefresh) {
            page = 1
            hasMoreData = true
            requestListCache.clear()
        }

        uiStateMachine.updateRequestList(requestListCache.toList(), isLoading = true)

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            donationRequestRepo.getAll(page, forceRefresh).onSuccess { list ->
                if (list.isEmpty()) {
                    hasMoreData = false
                } else {
                    page++
                    requestListCache.addAll(list)
                }
                uiStateMachine.updateRequestList(requestListCache.toList(), isLoading = false)
            }.onFailure {
                val message = it.message ?: "Failed to get data"
                uiStateMachine.updateLoadError(message)
            }
        }
    }
}
