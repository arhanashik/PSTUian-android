package com.workfort.pstuian.app.ui.donate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.DonationInput
import com.workfort.pstuian.model.DonationInputValidationError
import com.workfort.pstuian.repository.DonationRepository
import com.workfort.pstuian.sharedpref.Prefs
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DonateViewModel(
    private val donationRepo: DonationRepository,
    private val reducer: DonateScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<DonateScreenState> get() = _screenState

    private fun updateScreenState(update: DonateScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(DonateScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(DonateScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        DonateScreenStateUpdate.NavigateTo(
            DonateScreenState.NavigationState.GoBack,
        ),
    )

    fun onChangeInput(input: DonationInput) {
        updateScreenState(DonateScreenStateUpdate.SetDonationInput(input))
    }


     fun loadDonationOptions() {
         updateScreenState(DonateScreenStateUpdate.ShowLoading(true))
         viewModelScope.launch {
             runCatching {
                 val option = donationRepo.getDonationOption()
                 updateScreenState(DonateScreenStateUpdate.ShowLoading(false))
                 updateScreenState(DonateScreenStateUpdate.SetDonationOption(option))
             }.onFailure {
                 updateScreenState(DonateScreenStateUpdate.ShowLoading(false))
                 val message = it.message ?: "Could not get donation data. Please try again."
                 updateScreenState(
                     DonateScreenStateUpdate.UpdateMessageState(
                         DonateScreenState.DisplayState.MessageState.Error(message),
                     ),
                 )
             }
         }
    }

    fun sendDonationInfo() {
        val input = _screenState.value.displayState.donationInput
        val validationError = input.validate()
        updateScreenState(DonateScreenStateUpdate.SetValidationError(validationError))
        if (validationError.isNotEmpty()) {
            return
        }

        updateScreenState(DonateScreenStateUpdate.ShowLoading(true))
        viewModelScope.launch {
            runCatching {
                val response = donationRepo.saveDonation(
                    name = input.name,
                    email = input.email,
                    reference = input.reference,
                    info = input.message,
                )
                Prefs.donationId = response.toString()
                updateScreenState(DonateScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    DonateScreenStateUpdate.UpdateMessageState(
                        DonateScreenState.DisplayState.MessageState.SendDonationSuccess(
                            "Donation is under review! Thanks for your help."
                        ),
                    ),
                )
            }.onFailure {
                updateScreenState(DonateScreenStateUpdate.ShowLoading(false))
                val message = it.message ?: "Failed. Please try again."
                updateScreenState(
                    DonateScreenStateUpdate.UpdateMessageState(
                        DonateScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    private fun DonationInput.validate(): DonationInputValidationError {
        return DonationInputValidationError(
            name = if (name.isEmpty()) "*Required" else "",
            email = if (email.isEmpty()) {
                "*Required"
            }  else if (email.isValidEmail().not()) {
                "*Invalid email address"
            } else {
                ""
            },
            reference = if (reference.isEmpty()) "*Required" else "",
            message = if (message.isEmpty()) {
                "*Required"
            }  else if (message.length > 500) {
                "*Message too long"
            } else {
                ""
            },
        )
    }
}