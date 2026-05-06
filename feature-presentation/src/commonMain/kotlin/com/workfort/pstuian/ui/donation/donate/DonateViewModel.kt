package com.workfort.pstuian.ui.donation.donate

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.DonationInput
import com.workfort.pstuian.featuredomain.model.DonationInputValidationError
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.DonationRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.donation.donate.state.DonateMessageState
import com.workfort.pstuian.ui.donation.donate.state.DonateNavigationState
import com.workfort.pstuian.ui.donation.donate.state.DonateUiEvent
import com.workfort.pstuian.ui.donation.donate.state.DonateUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DonateViewModel(
    private val donationRepository: DonationRepository,
    private val screenData: SharedScreenData,
    private val uiStateMachine: DonateUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<DonateUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<DonateMessageState?>(null)
    val message: StateFlow<DonateMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<DonateNavigationState?>(null)
    val navigation: StateFlow<DonateNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        val donationOptions = screenData.getAppConfig()?.donationOptions ?: run {
            val message = "Could not get donation data. Please try again."
            _message.update { DonateMessageState.Error(message) }
            return
        }
        uiStateMachine.setDonationOption(donationOptions)
    }

    fun onUiEvent(event: DonateUiEvent) {
        when (event) {
            is DonateUiEvent.BackClicked -> onClickBack()
            is DonateUiEvent.ChangeInput -> onChangeInput(event.input)
            is DonateUiEvent.SendDonationClicked -> sendDonationInfo(event.input)
        }
    }

    fun onMessageHandled() = _message.update { null}

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { DonateNavigationState.GoBack }

    private fun onChangeInput(input: DonationInput) {
        uiStateMachine.setDonationInput(input)
    }

    fun sendDonationInfo(input: DonationInput) {
        val validationError = input.validate()
        uiStateMachine.setValidationError(validationError)
        if (validationError.isNotEmpty()) {
            return
        }

        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            donationRepository.saveDonation(
                name = input.name,
                email = input.email,
                reference = input.reference,
                info = input.message,
            ).onSuccess {
                uiStateMachine.showLoading(false)
                _message.update {
                    DonateMessageState.Success(
                        cancelable = false,
                        message = "Donation is under review! Thanks for your help.",
                    ) {
                        _navigation.update { DonateNavigationState.GoBack }
                    }
                }
            }.onFailure {
                uiStateMachine.showLoading(false)
                val message = it.message ?: "Failed. Please try again."
                _message.update { DonateMessageState.Error(message) }
            }
        }
    }

    private fun DonationInput.validate(): DonationInputValidationError {
        return DonationInputValidationError(
            name = if (name.isEmpty()) "*Required" else "",
            email = if (email.isEmpty()) {
                "*Required"
            } else if (email.isValidEmail().not()) {
                "*Invalid email address"
            } else {
                ""
            },
            reference = if (reference.isEmpty()) "*Required" else "",
            message = if (message.isEmpty()) {
                "*Required"
            } else if (message.length > 500) {
                "*Message too long"
            } else {
                ""
            },
        )
    }
}
