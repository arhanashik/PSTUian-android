package com.workfort.pstuian.ui.blooddonation.blooddonationcreate

import com.workfort.pstuian.ui.blooddonation.blooddonationcreate.state.BloodDonationCreateUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BloodDonationCreateUiStateMachine : UiStateMachine<BloodDonationCreateUiState> {

    private val _state = MutableStateFlow<BloodDonationCreateUiState>(BloodDonationCreateUiState.None)
    override val uiState: StateFlow<BloodDonationCreateUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: BloodDonationCreateUiState.() -> BloodDonationCreateUiState,
    ) = _state.update(updater)

    fun showLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is BloodDonationCreateUiState.None -> this
            is BloodDonationCreateUiState.Content -> copy(isOperationLoading = isLoading)
        }
    }

    fun setInitialContent() = updateUiState {
        BloodDonationCreateUiState.Content()
    }

    fun updateRequestId(requestId: Int) = updateUiState {
        when (this) {
            is BloodDonationCreateUiState.None -> this
            is BloodDonationCreateUiState.Content -> copy(
                requestId = requestId,
                enableSendButton = formattedDate.isNotEmpty(),
            )
        }
    }

    fun updateDate(date: Long?, formattedDate: String) = updateUiState {
        when (this) {
            is BloodDonationCreateUiState.None -> this
            is BloodDonationCreateUiState.Content -> copy(
                date = date,
                formattedDate = formattedDate,
                enableSendButton = formattedDate.isNotEmpty(),
            )
        }
    }

    fun updateInfo(info: String) = updateUiState {
        when (this) {
            is BloodDonationCreateUiState.None -> this
            is BloodDonationCreateUiState.Content -> copy(
                info = info,
                enableSendButton = formattedDate.isNotEmpty(),
            )
        }
    }
}
