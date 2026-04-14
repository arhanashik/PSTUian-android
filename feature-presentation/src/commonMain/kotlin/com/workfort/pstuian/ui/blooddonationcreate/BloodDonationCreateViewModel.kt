package com.workfort.pstuian.ui.blooddonationcreate

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository
import com.workfort.pstuian.ui.blooddonationcreate.state.BloodDonationCreateMessageState
import com.workfort.pstuian.ui.blooddonationcreate.state.BloodDonationCreateNavigationState
import com.workfort.pstuian.ui.blooddonationcreate.state.BloodDonationCreateUiEvent
import com.workfort.pstuian.ui.blooddonationcreate.state.BloodDonationCreateUiState
import com.workfort.pstuian.util.DateTimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class BloodDonationCreateViewModel(
    private val repo: BloodDonationRepository,
    private val dateTimeUtil: DateTimeUtil,
    private val uiStateMachine: BloodDonationCreateUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<BloodDonationCreateUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<BloodDonationCreateMessageState?>(null)
    val message: StateFlow<BloodDonationCreateMessageState?> = _message

    private val _navigation = MutableStateFlow<BloodDonationCreateNavigationState?>(null)
    val navigation: StateFlow<BloodDonationCreateNavigationState?> = _navigation

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
    }

    fun onUiEvent(event: BloodDonationCreateUiEvent) {
        when (event) {
            is BloodDonationCreateUiEvent.BackClicked -> onClickBack()
            is BloodDonationCreateUiEvent.RequestIdChanged -> onClickSelectDate()
            is BloodDonationCreateUiEvent.SelectDateClicked -> onClickSelectDate()
            is BloodDonationCreateUiEvent.InfoChanged -> onInfoChanged(event.info)
            is BloodDonationCreateUiEvent.SendClicked -> onSendClicked()
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { BloodDonationCreateNavigationState.GoBack }
    }

    private fun onClickSelectDate() {
        _message.update {
            // only allow dates until today
            BloodDonationCreateMessageState.SelectDate(
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
        val input = uiState.value as? BloodDonationCreateUiState.Content ?: return
        val requestId = input.requestId
        val date = input.date ?: return
        val info = input.info

        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                repo.insert(requestId, date, info)
                uiStateMachine.showLoading(false)
                _message.update {
                    BloodDonationCreateMessageState.Snackbar("Donation created successfully!")
                }
                _navigation.update { BloodDonationCreateNavigationState.GoBack }
            }.onFailure {
                uiStateMachine.showLoading(false)
                val message = it.message ?: "Failed to send the message. Please try again."
                _message.update { BloodDonationCreateMessageState.Error(message) }
            }
        }
    }
}