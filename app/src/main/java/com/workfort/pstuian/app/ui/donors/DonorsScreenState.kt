package com.workfort.pstuian.app.ui.donors

import com.workfort.pstuian.model.DonorEntity


data class DonorsScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val donorListState: DonorListState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                donorListState = DonorListState.None,
                messageState = null,
            )
        }
        sealed interface DonorListState {
            data object None : DonorListState
            data class Available(
                val donorList: List<DonorEntity>,
                val isLoading: Boolean,
            ) : DonorListState
            data class Error(val message: String) : DonorListState
        }
        sealed interface MessageState {
            data class ShowDetails(val item: DonorEntity) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data object DonateScreen : NavigationState
    }
}