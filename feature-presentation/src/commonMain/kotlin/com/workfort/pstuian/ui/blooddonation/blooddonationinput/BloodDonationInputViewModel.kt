package com.workfort.pstuian.ui.blooddonation.blooddonationinput

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputData
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputNavigationState
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.util.DateTimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BloodDonationInputViewModel(
    private val donationId: Int?,
    private val userId: Int,
    private val userType: UserType,
    private val bloodDonationRepository: BloodDonationRepository,
    private val dateTimeUtil: DateTimeUtil,
    private val uiStateMachine: BloodDonationInputUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<BloodDonationInputUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<BloodDonationInputMessageState?>(null)
    val message: StateFlow<BloodDonationInputMessageState?> = _message

    private val _navigation = MutableStateFlow<BloodDonationInputNavigationState?>(null)
    val navigation: StateFlow<BloodDonationInputNavigationState?> = _navigation

    override fun onUiReady() {
        val title = if (donationId == null) "Create Donation" else "Update Donation"
        uiStateMachine.setInitialContent(title)
    }

    fun onUiEvent(event: BloodDonationInputUiEvent) {
        when (event) {
            is BloodDonationInputUiEvent.BackClicked -> _navigation.update { BloodDonationInputNavigationState.GoBack }
            is BloodDonationInputUiEvent.RequestIdChanged -> uiStateMachine.updateRequestId(event.requestId)
            is BloodDonationInputUiEvent.SelectDateClicked -> onClickSelectDate()
            is BloodDonationInputUiEvent.InfoChanged -> onInfoChanged(event.info)
            is BloodDonationInputUiEvent.SendClicked -> onSendClicked(event.input)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickSelectDate() {
        _message.update {
            // only allow dates until today
            BloodDonationInputMessageState.SelectDate(
                allowedDateTill = dateTimeUtil.getTimeInMillsUntilMidnight(),
            ) { dateMills ->
                val formattedDate = if (dateMills == null) "" else {
                    dateTimeUtil.formatDateYYYYMMDD(dateMills)
                }
                uiStateMachine.updateDate(formattedDate)
            }
        }
    }

    private fun onInfoChanged(info: String) {
        uiStateMachine.updateInfo(info)
    }

    private fun onSendClicked(input: BloodDonationInputData) {
        if (input.hasError()) {
            _message.update { BloodDonationInputMessageState.Error("Please input valid data and try again") }
            return
        }

        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            bloodDonationRepository.insert(input.requestId, userId, userType, input.formattedDate, input.info)
                .onSuccess {
                    uiStateMachine.showLoading(false)
                    _message.update { BloodDonationInputMessageState.Snackbar("Donation created successfully!") }
                    _navigation.update { BloodDonationInputNavigationState.GoBack }
                }
                .onFailure {
                    uiStateMachine.showLoading(false)
                    val message = it.message ?: "Failed to create"
                    _message.update { BloodDonationInputMessageState.Error(message) }
                }
        }
    }
}