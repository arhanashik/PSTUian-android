package com.workfort.pstuian.app.ui.commonmodel

import kotlinx.coroutines.CoroutineScope

expect abstract class BaseViewModel() {
    val viewModelScope: CoroutineScope
    fun onClear()
}
