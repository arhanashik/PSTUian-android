package com.workfort.pstuian.util.flow

import com.workfort.pstuian.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.StateFlow

class ViewModelWrapper<T : Any>(
    val instance: BaseViewModel,
    val state: CommonFlow<T>
)
