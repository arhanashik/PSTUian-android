package com.workfort.pstuian.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

actual abstract class BaseViewModel {
    actual val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    actual fun onClear() {
        viewModelScope.cancel()
    }
}
