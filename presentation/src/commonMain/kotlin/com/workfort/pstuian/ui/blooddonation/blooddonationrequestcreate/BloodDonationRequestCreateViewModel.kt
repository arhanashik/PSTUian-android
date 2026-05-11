package com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInput
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInputError
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.BloodDonationRequestRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateNavigationState
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.util.DateTimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BloodDonationRequestCreateViewModel(
    private val bloodDonationRequestRepository: BloodDonationRequestRepository,
    private val sharedScreenData: SharedScreenData,
    private val dateTimeUtil: DateTimeUtil,
    private val uiStateMachine: BloodDonationRequestCreateUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<BloodDonationRequestCreateUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<BloodDonationRequestCreateMessageState?>(null)
    val message: StateFlow<BloodDonationRequestCreateMessageState?> = _message

    private val _navigation = MutableStateFlow<BloodDonationRequestCreateNavigationState?>(null)
    val navigation: StateFlow<BloodDonationRequestCreateNavigationState?> = _navigation

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
    }

    fun onUiEvent(event: BloodDonationRequestCreateUiEvent) {
        when (event) {
            is BloodDonationRequestCreateUiEvent.BackClicked -> onClickBack()
            is BloodDonationRequestCreateUiEvent.SelectDateClicked -> onClickSelectDate()
            is BloodDonationRequestCreateUiEvent.InputChanged -> uiStateMachine.updateInput(event.input)
            is BloodDonationRequestCreateUiEvent.SendClicked -> onSendClicked(event.input)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { BloodDonationRequestCreateNavigationState.GoBack }
    }

    private fun onClickSelectDate() {
        _message.update {
            BloodDonationRequestCreateMessageState.SelectDate(
                allowedDateFrom = dateTimeUtil.getTimeInMillsUntilMidnight(),
            ) { dateMills ->
                val date = if (dateMills == null) "" else {
                    dateTimeUtil.formatDateYYYYMMDD(dateMills)
                }
                val content = uiState.value as? BloodDonationRequestCreateUiState.Content ?: return@SelectDate
                uiStateMachine.updateInput(content.input.copy(date = date))
            }
        }
    }

    private fun onSendClicked(input: BloodDonationRequestInput) {
        val userId = sharedScreenData.getCurrentUser()?.userId ?: return
        val userType = sharedScreenData.getCurrentUserType() ?: return

        val validationError = input.validate()
        uiStateMachine.updateValidationError(validationError)
        if (validationError.hasError()) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showLoading(true)
            bloodDonationRequestRepository.insert(
                userId = userId,
                userType = userType,
                bloodGroup = input.bloodGroup,
                beforeDate = input.date,
                contact = input.contact,
                info = input.message,
            ).onSuccess {
                uiStateMachine.showLoading(false)
                _message.update { BloodDonationRequestCreateMessageState.Snackbar("Request created successfully!") }
                _navigation.update { BloodDonationRequestCreateNavigationState.GoBack }
            }.onFailure {
                uiStateMachine.showLoading(false)
                val message = it.message ?: "Request Failed. Please try again"
                _message.update { BloodDonationRequestCreateMessageState.Error(message) }
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
            } else if (message.length > 500) {
                "*Message too long"
            } else {
                ""
            },
        )
    }
}
