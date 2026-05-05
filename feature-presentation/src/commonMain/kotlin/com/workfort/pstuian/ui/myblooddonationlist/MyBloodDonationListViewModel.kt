package com.workfort.pstuian.ui.myblooddonationlist

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListMessageState
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListNavigationState
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListUiEvent
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyBloodDonationListViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val donationRepo: BloodDonationRepository,
    private val uiStateMachine: MyBloodDonationListUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<MyBloodDonationListUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<MyBloodDonationListMessageState?>(null)
    val message: StateFlow<MyBloodDonationListMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<MyBloodDonationListNavigationState?>(null)
    val navigation: StateFlow<MyBloodDonationListNavigationState?> = _navigation.asStateFlow()

    private var page = 0

    override fun onUiReady() {
        onUiEvent(MyBloodDonationListUiEvent.LoadList(refresh = true))
    }

    fun onUiEvent(event: MyBloodDonationListUiEvent) {
        viewModelScope.launch {
            when (event) {
                is MyBloodDonationListUiEvent.BackClicked -> onClickBack()
                is MyBloodDonationListUiEvent.CreateRequestClicked -> onClickCreateRequest()
                is MyBloodDonationListUiEvent.EditClicked -> onClickEdit(event.item)
                is MyBloodDonationListUiEvent.DeleteClicked -> onClickDelete(event.item)
                is MyBloodDonationListUiEvent.ConfirmDeleteClicked -> deleteDonation(event.item)
                is MyBloodDonationListUiEvent.LoadList -> loadDonationList(event.refresh)
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { MyBloodDonationListNavigationState.GoBack }

    private fun onClickCreateRequest() {
        _navigation.update { MyBloodDonationListNavigationState.GoToCreateBloodDonationRequest }
    }

    private fun onClickEdit(item: BloodDonationEntity) {
        _navigation.update {
            MyBloodDonationListNavigationState.GoToEditBloodDonationRequest(item.id)
        }
    }

    private fun onClickDelete(item: BloodDonationEntity) {
        _message.update {
            MyBloodDonationListMessageState.ConfirmDelete {
                deleteDonation(item)
            }
        }
    }

    private suspend fun loadDonationList(refresh: Boolean) {
        val currentState = uiStateMachine.uiState.value
        if (currentState.isLoading || (refresh.not() && currentState.isEndOfData)) {
            return
        }

        if (refresh) {
            page = 0
        }

        page += 1

        val currentItems = if (refresh) emptyList() else currentState.donations
        uiStateMachine.updateLoading(isLoading = true, isRefresh = refresh)

        runCatching {
            donationRepo.getAll(userId, userType.type, page)
        }.onSuccess { newList ->
            val allItems = currentItems + newList
            uiStateMachine.updateData(donations = allItems, isEndOfData = newList.isEmpty())
        }.onFailure {
            val message = it.message ?: "Failed to get data"
            uiStateMachine.updateError(message = message, isFirstPage = currentItems.isEmpty())
        }
    }

    private fun deleteDonation(item: BloodDonationEntity) {
        _message.update { MyBloodDonationListMessageState.Loading(cancelable = false) }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                donationRepo.delete(item.id)
            }.onSuccess {
                _message.update {
                    MyBloodDonationListMessageState.Success("Deleted successfully")
                }
                // Refresh list after deletion
                loadDonationList(refresh = true)
            }.onFailure {
                val message = it.message ?: "Failed to delete"
                _message.update { MyBloodDonationListMessageState.Error(message) }
            }
        }
    }
}
