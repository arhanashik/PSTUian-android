package com.workfort.pstuian.app.ui.blooddonationcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.BloodDonationInput
import com.workfort.pstuian.model.BloodDonationInputError
import com.workfort.pstuian.repository.BloodDonationRepository
import com.workfort.pstuian.util.DateUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BloodDonationCreateViewModel(
    private val repo: BloodDonationRepository,
    private val reducer: BloodDonationCreateScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<BloodDonationCreateScreenState> get() = _screenState

    private fun updateScreenState(update: BloodDonationCreateScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(BloodDonationCreateScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(BloodDonationCreateScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        BloodDonationCreateScreenStateUpdate.NavigateTo(
            BloodDonationCreateScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickSelectDate() = updateScreenState(
        BloodDonationCreateScreenStateUpdate.UpdateMessageState(
            BloodDonationCreateScreenState.DisplayState.MessageState.SelectDate,
        ),
    )

    fun onChangeInput(input: BloodDonationInput) {
        updateScreenState(BloodDonationCreateScreenStateUpdate.SetInput(input))
        updateScreenState(
            BloodDonationCreateScreenStateUpdate.SetValidationError(input.validate()),
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
        updateScreenState(BloodDonationCreateScreenStateUpdate.SetValidationError(validationError))
        if (validationError.isNotEmpty()) {
            return
        }
        updateScreenState(BloodDonationCreateScreenStateUpdate.ShowLoading(true))
        viewModelScope.launch {
            runCatching {
                repo.insert(
                    requestId = input.requestId,
                    date = input.date,
                    info = input.message,
                )
                updateScreenState(
                    BloodDonationCreateScreenStateUpdate.ShowLoading(false),
                )
                updateScreenState(
                    BloodDonationCreateScreenStateUpdate.UpdateMessageState(
                        BloodDonationCreateScreenState.DisplayState.MessageState
                            .SendRequestSuccess("Donation created successfully!"),
                    ),
                )
            }.onFailure {
                updateScreenState(BloodDonationCreateScreenStateUpdate.ShowLoading(false))
                val message = it.message ?: "Failed to send the message. Please try again."
                updateScreenState(
                    BloodDonationCreateScreenStateUpdate.UpdateMessageState(
                        BloodDonationCreateScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    private fun BloodDonationInput.validate(): BloodDonationInputError {
        return BloodDonationInputError(
            requestId = "",
            date = if (date.isEmpty()) "*Required" else "",
            message = if (message.length > 500) {
                "*Message too long"
            } else {
                ""
            },
        )
    }
}