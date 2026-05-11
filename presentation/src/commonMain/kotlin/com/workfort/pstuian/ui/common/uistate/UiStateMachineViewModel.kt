package com.workfort.pstuian.ui.common.uistate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

abstract class UiStateMachineViewModel<UiState>(
    private val uiStateMachine: UiStateMachine<UiState>,
    initializationMode: InitializationMode = InitializationMode.JustOnce,
) : ViewModel() {
    // initialization method is called automatically for modes other than Manual
    val uiState: StateFlow<UiState> = uiStateMachine.uiState.run {
        val sharingStartedMode = when (initializationMode) {
            is InitializationMode.Manual -> return@run this
            is InitializationMode.JustOnce -> SharingStarted.Lazily
            is InitializationMode.Custom -> initializationMode.sharingStartedMode
        }

        // By taking advantage of the view consuming ui state during the view creation and
        // initialization of the viewmodel we trigger the initialization method [onUiReady]
        // without the view explicitly doing/knowing anything.
        onStart { onUiReady() }.stateIn(
            scope = viewModelScope,
            started = sharingStartedMode,
            initialValue = value,
        )
    }

    /**
     * A function that should be called when the view is ready to render the UI.
     * It should prepare the ui data for the initial render.
     *
     * Some common use cases:
     * - showing loading indicator
     * - starting performance/analytics tracking
     * - start api calls
     */
    abstract fun onUiReady()
}
