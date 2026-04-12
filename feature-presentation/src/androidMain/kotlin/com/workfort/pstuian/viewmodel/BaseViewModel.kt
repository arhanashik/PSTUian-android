package com.workfort.pstuian.app.ui.commonmodel

import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope as androidViewModelScope

actual abstract class BaseViewModel : ViewModel() {
    actual val viewModelScope: CoroutineScope = androidViewModelScope

    actual fun onClear() {
        onCleared()
    }
}
