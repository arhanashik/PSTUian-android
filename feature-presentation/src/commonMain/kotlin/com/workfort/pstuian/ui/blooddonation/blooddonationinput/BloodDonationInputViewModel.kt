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

        donationId?.let { loadData(it) } // Update flow - load data to update
    }

    fun onUiEvent(event: BloodDonationInputUiEvent) {
        when (event) {
            is BloodDonationInputUiEvent.BackClicked -> _navigation.update { BloodDonationInputNavigationState.GoBack }
            is BloodDonationInputUiEvent.InputChanged -> uiStateMachine.updateInputData(event.input)
            is BloodDonationInputUiEvent.SelectDateClicked -> onClickSelectDate(event.input)
            is BloodDonationInputUiEvent.SendClicked -> onSendClicked(event.input)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun loadData(donationId: Int) {
        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            bloodDonationRepository.get(donationId).onSuccess {
                uiStateMachine.updateInputData(
                    BloodDonationInputData(
                        requestId = it.requestId ?: 0,
                        formattedDate = it.date.split(" ").firstOrNull() ?: "",
                        info = it.info ?: "",
                    )
                )
            }.onFailure {
                uiStateMachine.showLoading(false)
                val message = it.message ?: "Failed to load data"
                _message.update { BloodDonationInputMessageState.Snackbar(message) }
                _navigation.update { BloodDonationInputNavigationState.GoBack }
            }
        }
    }

    private fun onClickSelectDate(input: BloodDonationInputData) {
        _message.update {
            // only allow dates until today
            BloodDonationInputMessageState.SelectDate(
                allowedDateTill = dateTimeUtil.getTimeInMillsUntilMidnight(),
            ) { dateMills ->
                val formattedDate = if (dateMills == null) "" else {
                    dateTimeUtil.formatDateYYYYMMDD(dateMills)
                }
                uiStateMachine.updateInputData(input.copy(formattedDate = formattedDate))
            }
        }
    }

    private fun onSendClicked(input: BloodDonationInputData) {
        if (input.hasError()) {
            _message.update { BloodDonationInputMessageState.Error("Please input valid data and try again") }
            return
        }

        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            if (donationId == null) { // new entry flow
                bloodDonationRepository.insert(input.requestId, userId, userType, input.formattedDate, input.info)
            } else { // update flow
                bloodDonationRepository.update(donationId, input.requestId, input.formattedDate, input.info)
            }.onSuccess {
                uiStateMachine.showLoading(false)
                _message.update { BloodDonationInputMessageState.Snackbar("Action successful!") }
                _navigation.update { BloodDonationInputNavigationState.GoBack }
            }.onFailure {
                uiStateMachine.showLoading(false)
                val message = it.message ?: "Action failed. Please try again"
                _message.update { BloodDonationInputMessageState.Error(message) }
            }
        }
    }
}