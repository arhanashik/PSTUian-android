package com.workfort.pstuian.ui.blooddonation.blooddonationinput

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository
import com.workfort.pstuian.model.SharedScreenData
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
    private val bloodDonationRepository: BloodDonationRepository,
    private val sharedScreenData: SharedScreenData,
    private val dateTimeUtil: DateTimeUtil,
    private val uiStateMachine: BloodDonationInputUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<BloodDonationInputUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<BloodDonationInputMessageState?>(null)
    val message: StateFlow<BloodDonationInputMessageState?> = _message

    private val _navigation = MutableStateFlow<BloodDonationInputNavigationState?>(null)
    val navigation: StateFlow<BloodDonationInputNavigationState?> = _navigation

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
    }

    fun onUiEvent(event: BloodDonationInputUiEvent) {
        when (event) {
            is BloodDonationInputUiEvent.BackClicked -> onClickBack()
            is BloodDonationInputUiEvent.RequestIdChanged -> onClickSelectDate()
            is BloodDonationInputUiEvent.SelectDateClicked -> onClickSelectDate()
            is BloodDonationInputUiEvent.InfoChanged -> onInfoChanged(event.info)
            is BloodDonationInputUiEvent.SendClicked -> onSendClicked()
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { BloodDonationInputNavigationState.GoBack }
    }

    private fun onClickSelectDate() {
        _message.update {
            // only allow dates until today
            BloodDonationInputMessageState.SelectDate(
                allowedDateTill = dateTimeUtil.getTimeInMillsUntilMidnight(),
            ) { dateMills ->
                val formattedDate = if (dateMills == null) "" else {
                    dateTimeUtil.formatDateDDMMMYYYY(dateMills)
                }
                uiStateMachine.updateDate(dateMills, formattedDate)
            }
        }
    }

    private fun onInfoChanged(info: String) {
        uiStateMachine.updateInfo(info)
    }

    private fun onSendClicked() {
        val input = uiState.value as? BloodDonationInputUiState.Content ?: return
        val requestId = input.requestId
        val userId = sharedScreenData.getCurrentUser()?.userId ?: return
        val userType = sharedScreenData.getCurrentUserType() ?: return
        val date = input.date ?: return
        val info = input.info

        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                bloodDonationRepository.insert(requestId, userId, userType, date, info)
                uiStateMachine.showLoading(false)
                _message.update {
                    BloodDonationInputMessageState.Snackbar("Donation created successfully!")
                }
                _navigation.update { BloodDonationInputNavigationState.GoBack }
            }.onFailure {
                uiStateMachine.showLoading(false)
                val message = it.message ?: "Failed to send the message. Please try again."
                _message.update { BloodDonationInputMessageState.Error(message) }
            }
        }
    }
}