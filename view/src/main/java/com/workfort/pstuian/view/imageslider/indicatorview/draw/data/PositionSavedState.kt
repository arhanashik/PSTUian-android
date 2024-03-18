package com.workfort.pstuian.view.imageslider.indicatorview.draw.data

import android.os.Parcelable
import android.view.View.BaseSavedState
import kotlinx.parcelize.Parcelize

@Parcelize
data class PositionSavedState(
    var source: Parcelable? = null,
    var selectedPosition: Int = 0,
    var selectingPosition: Int = 0,
    var lastSelectedPosition: Int = 0,
) : BaseSavedState(source), Parcelable