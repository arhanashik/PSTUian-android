package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.BloodDonationRequestRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.screendata.BloodDonationRequestDisplayData
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
    private val sharedScreenData: SharedScreenData,
    private val bloodDonationRequestDisplayDataMapper: BloodDonationRequestDisplayDataMapper,
    private val uiStateMachine: BloodDonationRequestListUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<BloodDonationRequestListUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<BloodDonationRequestListMessageState?>(null)
    val message: StateFlow<BloodDonationRequestListMessageState?> = _message

    private val _navigation = MutableStateFlow<BloodDonationRequestListNavigationState?>(null)
    val navigation: StateFlow<BloodDonationRequestListNavigationState?> = _navigation

    private var page = 1
    private var hasMoreData = true
    private val requestListCache = arrayListOf<BloodDonationRequestDisplayData>()

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
            is BloodDonationRequestListUiEvent.MarkAsCompleteClicked -> onClickMarkAsComplete(event.item)
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

    private fun onClickItem(item: BloodDonationRequestDisplayData) {
        _message.update { BloodDonationRequestListMessageState.ShowDetails(item.bloodDonationRequest) }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update { BloodDonationRequestListMessageState.Call(phoneNumber) }
    }

    private fun onClickMarkAsComplete(item: BloodDonationRequestDisplayData) {
        val userId = sharedScreenData.getCurrentUser()?.userId ?: return
        val userType = sharedScreenData.getCurrentUserType() ?: return

        _message.update {
            BloodDonationRequestListMessageState.Confirm("Are you surely want to mark the item as complete") {
                viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                    // TODO show operation loading
                    donationRequestRepo.markAsComplete(item.bloodDonationRequest.id, userId, userType)
                        .onSuccess {
                            _message.update { BloodDonationRequestListMessageState.Snackbar("Marked as completed!") }
                            loadDonationRequests(forceRefresh = true)
                        }
                        .onFailure {
                            val message = it.message ?: "Failed to mark as complete"
                            _message.update { BloodDonationRequestListMessageState.Snackbar(message) }
                        }
                }
            }
        }
    }

    private fun loadDonationRequests(forceRefresh: Boolean) {
        val userId = sharedScreenData.getCurrentUser()?.userId ?: return
        val userType = sharedScreenData.getCurrentUserType() ?: return

        if (forceRefresh) {
            page = 1
            hasMoreData = true
            requestListCache.clear()
        } else if (!hasMoreData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showLoading(true)
            donationRequestRepo.getAll(userId, userType, page, forceRefresh).onSuccess { list ->
                if (list.isEmpty()) {
                    hasMoreData = false
                } else {
                    page++
                    requestListCache.addAll(
                        bloodDonationRequestDisplayDataMapper.map( userId, userType, list),
                    )
                }
                uiStateMachine.updateRequestList(requestListCache)
            }.onFailure {
                uiStateMachine.showLoading(false)
                val message = it.message ?: "Failed to load data"
                uiStateMachine.updateLoadError(message)
            }
        }
    }
}
