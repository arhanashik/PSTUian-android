package com.workfort.pstuian.ui.donors

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.model.DonorEntity
import com.workfort.pstuian.featuredomain.repository.DonationRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.donors.state.DonorsMessageState
import com.workfort.pstuian.ui.donors.state.DonorsNavigationState
import com.workfort.pstuian.ui.donors.state.DonorsUiEvent
import com.workfort.pstuian.ui.donors.state.DonorsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DonorsViewModel(
    private val donationRepo: DonationRepository,
    private val uiStateMachine: DonorsUiStateMachine,
) : UiStateMachineViewModel<DonorsUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<DonorsMessageState?>(null)
    val message: StateFlow<DonorsMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<DonorsNavigationState?>(null)
    val navigation: StateFlow<DonorsNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        loadDonors()
    }

    fun onUiEvent(event: DonorsUiEvent) {
        when (event) {
            is DonorsUiEvent.BackClicked -> onClickBack()
            is DonorsUiEvent.Refresh -> loadDonors()
            is DonorsUiEvent.DonateClicked -> onClickDonate()
            is DonorsUiEvent.DonorClicked -> onDonorClicked(event.donor)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { DonorsNavigationState.GoBack }
    }

    private fun onClickDonate() {
        _navigation.update { DonorsNavigationState.DonateScreen }
    }

    private fun loadDonors() {
        viewModelScope.launch {
            uiStateMachine.showLoading()
            runCatching {
                donationRepo.getDonors()
            }.onSuccess {
                uiStateMachine.showContent(it)
            }.onFailure {
                uiStateMachine.showContent(emptyList())
                val message = it.message ?: "Failed to load donors"
                _message.update { DonorsMessageState.Error(message) }
            }
        }
    }

    private fun onDonorClicked(donor: DonorEntity) {
        _message.update { DonorsMessageState.ShowDonorDetails(donor) }
    }
}
