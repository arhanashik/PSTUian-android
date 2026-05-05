package com.workfort.pstuian.ui.blooddonation.blooddonationhistory

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryNavigationState
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BloodDonationHistoryViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val donationRepo: BloodDonationRepository,
    private val uiStateMachine: BloodDonationHistoryUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<BloodDonationHistoryUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<BloodDonationHistoryMessageState?>(null)
    val message: StateFlow<BloodDonationHistoryMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<BloodDonationHistoryNavigationState?>(null)
    val navigation: StateFlow<BloodDonationHistoryNavigationState?> = _navigation.asStateFlow()

    private var page = 0

    override fun onUiReady() {
        onUiEvent(BloodDonationHistoryUiEvent.LoadList(refresh = true))
    }

    fun onUiEvent(event: BloodDonationHistoryUiEvent) {
        viewModelScope.launch {
            when (event) {
                is BloodDonationHistoryUiEvent.BackClicked -> onClickBack()
                is BloodDonationHistoryUiEvent.CreateRequestClicked -> onClickCreateRequest()
                is BloodDonationHistoryUiEvent.EditClicked -> onClickEdit(event.item)
                is BloodDonationHistoryUiEvent.DeleteClicked -> onClickDelete(event.item)
                is BloodDonationHistoryUiEvent.ConfirmDeleteClicked -> deleteDonation(event.item)
                is BloodDonationHistoryUiEvent.LoadList -> loadDonationList(event.refresh)
            }
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

    private suspend fun loadDonationList(refresh: Boolean) {
        val currentState = uiStateMachine.uiState.value
        if (currentState.isLoading || (refresh.not() && currentState.isEndOfData)) {
            return
        }

        if (refresh) {
            page = 0
        }

        page += 1

        val currentItems = if (refresh) emptyList() else currentState.donations
        uiStateMachine.updateLoading(isLoading = true, isRefresh = refresh)

        runCatching {
            donationRepo.getAll(userId, userType.type, page)
        }.onSuccess { newList ->
            val allItems = currentItems + newList
            uiStateMachine.updateData(donations = allItems, isEndOfData = newList.isEmpty())
        }.onFailure {
            val message = it.message ?: "Failed to get data"
            uiStateMachine.updateError(message = message, isFirstPage = currentItems.isEmpty())
        }
    }

    private fun deleteDonation(item: BloodDonationEntity) {
        _message.update { BloodDonationHistoryMessageState.Loading(cancelable = false) }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                donationRepo.delete(item.id)
            }.onSuccess {
                _message.update {
                    BloodDonationHistoryMessageState.Success("Deleted successfully")
                }
                // Refresh list after deletion
                loadDonationList(refresh = true)
            }.onFailure {
                val message = it.message ?: "Failed to delete"
                _message.update { BloodDonationHistoryMessageState.Error(message) }
            }
        }
    }
}
