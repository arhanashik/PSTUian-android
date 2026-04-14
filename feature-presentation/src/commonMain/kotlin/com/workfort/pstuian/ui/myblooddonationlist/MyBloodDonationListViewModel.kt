package com.workfort.pstuian.ui.myblooddonationlist

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListUiEvent
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListUiState
import kotlinx.coroutines.launch

class MyBloodDonationListViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val donationRepo: BloodDonationRepository,
    private val stateMachine: MyBloodDonationListUiStateMachine,
) : UiStateMachineViewModel<MyBloodDonationListUiState>(stateMachine) {

    private var page = 0

    override fun onUiReady() {
        onUiEvent(MyBloodDonationListUiEvent.OnLoadList(refresh = true))
    }

    fun onUiEvent(event: MyBloodDonationListUiEvent) {
        viewModelScope.launch {
            when (event) {
                is MyBloodDonationListUiEvent.OnClickBack -> stateMachine.onClickBack()
                is MyBloodDonationListUiEvent.OnClickCreateRequest -> stateMachine.onClickCreateRequest()
                is MyBloodDonationListUiEvent.OnClickEdit -> stateMachine.onClickEdit(event.item)
                is MyBloodDonationListUiEvent.OnClickDelete -> stateMachine.onClickDelete(event.item)
                is MyBloodDonationListUiEvent.OnConfirmDelete -> deleteDonation(event.item)
                is MyBloodDonationListUiEvent.OnLoadList -> loadDonationList(event.refresh)
                is MyBloodDonationListUiEvent.MessageConsumed -> stateMachine.messageConsumed()
                is MyBloodDonationListUiEvent.NavigationConsumed -> stateMachine.navigationConsumed()
            }
        }
    }

    private suspend fun loadDonationList(refresh: Boolean) {
        val currentState = stateMachine.uiState.value
        if (currentState.isLoading || (refresh.not() && currentState.isEndOfData)) {
            return
        }

        if (refresh) {
            page = 0
        }

        page += 1

        val currentItems = if (refresh) emptyList() else currentState.donations
        stateMachine.updateLoading(isLoading = true, isRefresh = refresh)

        runCatching {
            donationRepo.getAll(userId, userType.type, page)
        }.onSuccess { newList ->
            val allItems = currentItems + newList
            stateMachine.updateData(donations = allItems, isEndOfData = newList.isEmpty())
        }.onFailure {
            val message = it.message ?: "Failed to get data"
            stateMachine.updateError(message = message, isFirstPage = currentItems.isEmpty())
        }
    }

    private suspend fun deleteDonation(item: BloodDonationEntity) {
        stateMachine.updateMessageState(MyBloodDonationListUiState.MessageState.Loading(cancelable = false))

        runCatching {
            donationRepo.delete(item.id)
        }.onSuccess {
            stateMachine.updateMessageState(
                MyBloodDonationListUiState.MessageState.Success("Deleted successfully")
            )
            // Refresh list after deletion
            loadDonationList(refresh = true)
        }.onFailure {
            val message = it.message ?: "Failed to delete"
            stateMachine.updateMessageState(MyBloodDonationListUiState.MessageState.Error(message))
        }
    }
}
