package com.workfort.pstuian.app.ui.blooddonationrequestcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.BloodDonationRequestInput
import com.workfort.pstuian.model.BloodDonationRequestInputError
import com.workfort.pstuian.repository.BloodDonationRequestRepository
import com.workfort.pstuian.util.DateUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BloodDonationRequestCreateViewModel(
    private val repo: BloodDonationRequestRepository,
    private val reducer: BloodDonationRequestCreateScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<BloodDonationRequestCreateScreenState> get() = _screenState

    private fun updateScreenState(update: BloodDonationRequestCreateScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(BloodDonationRequestCreateScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(BloodDonationRequestCreateScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        BloodDonationRequestCreateScreenStateUpdate.NavigateTo(
            BloodDonationRequestCreateScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickSelectDate() = updateScreenState(
        BloodDonationRequestCreateScreenStateUpdate.UpdateMessageState(
            BloodDonationRequestCreateScreenState.DisplayState.MessageState.SelectDate,
        ),
    )

    fun onChangeInput(input: BloodDonationRequestInput) {
        updateScreenState(BloodDonationRequestCreateScreenStateUpdate.SetInput(input))
        updateScreenState(
            BloodDonationRequestCreateScreenStateUpdate.SetValidationError(input.validate()),
        )
    }

    fun onSelectDate(dateMills: Long?) {
        val input = _screenState.value.displayState.input.copy(
            date = if (dateMills == null) "" else {
                DateUtil.format(dateMills, "yyyy-MM-dd")
            }
        )
        onChangeInput(input)
        messageConsumed()
    }

    fun sendRequest() {
        val input = _screenState.value.displayState.input
        val validationError = input.validate()
        updateScreenState(BloodDonationRequestCreateScreenStateUpdate.SetValidationError(validationError))
        if (validationError.isNotEmpty()) {
            return
        }
        updateScreenState(BloodDonationRequestCreateScreenStateUpdate.ShowLoading(true))
        viewModelScope.launch {
            runCatching {
                repo.insert(
                    bloodGroup = input.bloodGroup,
                    beforeDate = input.date,
                    contact = input.contact,
                    info = input.message,
                )
                updateScreenState(
                    BloodDonationRequestCreateScreenStateUpdate.ShowLoading(false),
                )
                updateScreenState(
                    BloodDonationRequestCreateScreenStateUpdate.UpdateMessageState(
                        BloodDonationRequestCreateScreenState.DisplayState.MessageState
                            .SendRequestSuccess("Request created successfully"),
                    ),
                )
            }.onFailure {
                updateScreenState(BloodDonationRequestCreateScreenStateUpdate.ShowLoading(false))
                val message = it.message ?: "Failed to send the message. Please try again."
                updateScreenState(
                    BloodDonationRequestCreateScreenStateUpdate.UpdateMessageState(
                        BloodDonationRequestCreateScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    private fun BloodDonationRequestInput.validate(): BloodDonationRequestInputError {
        return BloodDonationRequestInputError(
            bloodGroup = if (bloodGroup.isEmpty()) "*Required" else "",
            date = if (date.isEmpty()) "*Required" else "",
            contact = if (contact.isEmpty()) "*Required" else "",
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