package com.workfort.pstuian.viewmodel

import kotlinx.coroutines.CoroutineScope

expect abstract class BaseViewModel() {
    val viewModelScope: CoroutineScope
    fun onClear()
}
