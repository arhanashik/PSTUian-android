package com.workfort.pstuian.app.ui.donors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.DonorEntity
import com.workfort.pstuian.repository.DonationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DonorsViewModel(
    private val donationRepo: DonationRepository,
    private val reducer: DonorsScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<DonorsScreenState> get() = _screenState

    private fun updateScreenState(update: DonorsScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() {
        updateScreenState(DonorsScreenStateUpdate.MessageConsumed)
    }

    fun navigationConsumed() {
        updateScreenState(DonorsScreenStateUpdate.NavigationConsumed)
    }

    fun onClickBack() = updateScreenState(
        DonorsScreenStateUpdate.NavigateTo(
            DonorsScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickDonate() = updateScreenState(
        DonorsScreenStateUpdate.NavigateTo(DonorsScreenState.NavigationState.DonateScreen),
    )

    fun onClickItem(item: DonorEntity) = updateScreenState(
        DonorsScreenStateUpdate.UpdateMessageState(
            DonorsScreenState.DisplayState.MessageState.ShowDetails(item),
        ),
    )


    private val donorListCache = arrayListOf<DonorEntity>()
    fun loadDonors() {
        donorListCache.clear()
        updateScreenState(
            DonorsScreenStateUpdate.ShowDonorList(donorListCache, isLoading = true)
        )
        viewModelScope.launch {
            runCatching {
                val list = donationRepo.getDonors()
                donorListCache.addAll(list)
                updateScreenState(
                    DonorsScreenStateUpdate.ShowDonorList(donorListCache, isLoading = false)
                )
            }.onFailure {
                val message = it.message ?: "Failed to load data"
                updateScreenState(
                    DonorsScreenStateUpdate.DataLoadFailed(message)
                )
            }
        }
    }
}