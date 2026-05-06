package com.workfort.pstuian.ui.donation.donors

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Donor
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.DonationRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.donation.donors.state.DonorsMessageState
import com.workfort.pstuian.ui.donation.donors.state.DonorsNavigationState
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiEvent
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DonorsViewModel(
    private val donationRepository: DonationRepository,
    private val uiStateMachine: DonorsUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<DonorsUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<DonorsMessageState?>(null)
    val message: StateFlow<DonorsMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<DonorsNavigationState?>(null)
    val navigation: StateFlow<DonorsNavigationState?> = _navigation.asStateFlow()

    private var page = 1
    private val donorsCache = mutableListOf<Donor>()
    private var hasMoreData = true

    override fun onUiReady() {
        loadDonors()
    }

    fun onUiEvent(event: DonorsUiEvent) {
        when (event) {
            is DonorsUiEvent.BackClicked -> _navigation.update { DonorsNavigationState.GoBack }
            is DonorsUiEvent.DonateClicked -> onClickDonate()
            is DonorsUiEvent.DonorClicked -> onDonorClicked(event.donor)
            is DonorsUiEvent.LoadMore -> loadDonors()
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickDonate() {
        _navigation.update { DonorsNavigationState.DonateScreen }
    }

    private fun loadDonors(forceRefresh: Boolean = false) {
        if (forceRefresh) {
            page = 1
            donorsCache.clear()
            hasMoreData = true
        }
        if (!hasMoreData) return

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showLoading(isLoading = true)
            donationRepository.getDonors(page, forceRefresh)
                .onSuccess { list ->
                    if (list.isEmpty()) {
                        hasMoreData = false
                    } else {
                        page++
                        donorsCache.addAll(list)
                    }
                    uiStateMachine.showDoners(donors = donorsCache)
                }
                .onFailure {
                    uiStateMachine.showLoading(isLoading = false)
                    val message = it.message ?: "Failed to load donors ${it.code}"
                    _message.update { DonorsMessageState.Error(message) }
                }
        }
    }

    private fun onDonorClicked(donor: Donor) {
        _message.update { DonorsMessageState.ShowDonorDetails(donor) }
    }
}
