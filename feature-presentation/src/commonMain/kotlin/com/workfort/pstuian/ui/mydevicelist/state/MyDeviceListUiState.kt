package com.workfort.pstuian.ui.mydevicelist.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.DeviceEntity

@Immutable
data class MyDeviceListUiState(
    val devices: List<DeviceEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isEndOfData: Boolean = false,
    val error: String? = null,
)
