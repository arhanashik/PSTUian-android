package com.workfort.pstuian.ui.blooddonation.blooddonationhistory

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryNavigationState
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BloodDonationHistoryViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val donationRepo: BloodDonationRepository,
    private val uiStateMachine: BloodDonationHistoryUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<BloodDonationHistoryUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<BloodDonationHistoryMessageState?>(null)
    val message = _message.asStateFlow()

    private val _navigation = MutableStateFlow<BloodDonationHistoryNavigationState?>(null)
    val navigation = _navigation.asStateFlow()

    private var page = 1
    private var hasMoreData = true
    private val donationsCache = mutableListOf<BloodDonationEntity>()

    override fun onUiReady() {
        loadDonationList(forceRefresh = false)
    }

    fun onUiEvent(event: BloodDonationHistoryUiEvent) {
        when (event) {
            is BloodDonationHistoryUiEvent.BackClicked -> onClickBack()
            is BloodDonationHistoryUiEvent.CreateRequestClicked -> onClickCreateRequest()
            is BloodDonationHistoryUiEvent.EditClicked -> onClickEdit(event.item)
            is BloodDonationHistoryUiEvent.DeleteClicked -> onClickDelete(event.item)
            is BloodDonationHistoryUiEvent.LoadMore -> loadDonationList(forceRefresh = false)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { BloodDonationHistoryNavigationState.GoBack }

    private fun onClickCreateRequest() {
        _navigation.update { BloodDonationHistoryNavigationState.GoToCreateBloodDonationRequest }
    }

    private fun onClickEdit(item: BloodDonationEntity) {
        _navigation.update {
            BloodDonationHistoryNavigationState.GoToEditBloodDonationRequest(item.id)
        }
    }

    private fun onClickDelete(item: BloodDonationEntity) {
        _message.update {
            BloodDonationHistoryMessageState.ConfirmDelete {
                deleteDonation(item)
            }
        }
    }

    private fun loadDonationList(forceRefresh: Boolean) {
        if (forceRefresh) {
            donationsCache.clear()
            page = 1
            hasMoreData = true
        } else if (!hasMoreData) {
            return
        }

        uiStateMachine.updateContentLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            donationRepo.getAll(userId, userType.type, page, forceRefresh)
                .onSuccess { list ->
                    if (list.isEmpty()) {
                        hasMoreData = false
                    } else {
                        page++
                    }
                    donationsCache.addAll(list)
                    uiStateMachine.showDonations(donationsCache)
                }
                .onFailure {
                    uiStateMachine.updateContentLoading(false)
                    if (donationsCache.isEmpty()) {
                        val message = it.message ?: "Failed to get data"
                        uiStateMachine.showError(message)
                    }
                }
        }
    }

    private fun deleteDonation(item: BloodDonationEntity) {
        uiStateMachine.updateOperationLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            donationRepo.delete(item.id)
                .onSuccess {
                    uiStateMachine.updateOperationLoading(false)
                    _message.update {
                        BloodDonationHistoryMessageState.Success("Deleted successfully")
                    }
                    loadDonationList(forceRefresh = true)
                }
                .onFailure {
                    uiStateMachine.updateOperationLoading(false)
                    val message = it.message ?: "Failed to delete"
                    _message.update { BloodDonationHistoryMessageState.Error(message) }
                }
        }
    }
}
