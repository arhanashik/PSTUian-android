package com.workfort.pstuian.ui.imageupload

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiEvent
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiState
import kotlinx.coroutines.launch

class ImageUploadViewModel(
    val userId: Int,
    val userType: UserType,
    private val stateMachine: ImageUploadUiStateMachine,
) : UiStateMachineViewModel<ImageUploadUiState>(stateMachine) {

    override fun onUiReady() {}

    fun onUiEvent(event: ImageUploadUiEvent) {
        viewModelScope.launch {
            stateMachine.emit(event)
        }
    }
}
