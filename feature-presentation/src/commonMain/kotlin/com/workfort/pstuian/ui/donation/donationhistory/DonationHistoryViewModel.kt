package com.workfort.pstuian.ui.donation.donationhistory

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Donation
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.DonationRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.donation.donationhistory.state.DonationHistoryMessageState
import com.workfort.pstuian.ui.donation.donationhistory.state.DonationHistoryNavigationState
import com.workfort.pstuian.ui.donation.donationhistory.state.DonationHistoryUiEvent
import com.workfort.pstuian.ui.donation.donationhistory.state.DonationHistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DonationHistoryViewModel(
    private val donationRepository: DonationRepository,
    private val uiStateMachine: DonationHistoryUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<DonationHistoryUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<DonationHistoryMessageState?>(null)
    val message: StateFlow<DonationHistoryMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<DonationHistoryNavigationState?>(null)
    val navigation: StateFlow<DonationHistoryNavigationState?> = _navigation.asStateFlow()

    private var page = 1
    private val donorsCache = mutableListOf<Donation>()
    private var hasMoreData = true

    override fun onUiReady() {
        loadDonors()
    }

    fun onUiEvent(event: DonationHistoryUiEvent) {
        when (event) {
            is DonationHistoryUiEvent.BackClicked -> _navigation.update { DonationHistoryNavigationState.GoBack }
            is DonationHistoryUiEvent.DonateClicked -> onClickDonate()
            is DonationHistoryUiEvent.DonorClicked -> onDonorClicked(event.donation)
            is DonationHistoryUiEvent.LoadMore -> loadDonors()
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickDonate() {
        _navigation.update { DonationHistoryNavigationState.DonateScreen }
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
                    uiStateMachine.showDoners(donations = donorsCache)
                }
                .onFailure {
                    uiStateMachine.showLoading(isLoading = false)
                    val message = it.message ?: "Failed to load donors ${it.code}"
                    _message.update { DonationHistoryMessageState.Error(message) }
                }
        }
    }

    private fun onDonorClicked(donation: Donation) {
        _message.update { DonationHistoryMessageState.ShowDonorDetails(donation) }
    }
}
