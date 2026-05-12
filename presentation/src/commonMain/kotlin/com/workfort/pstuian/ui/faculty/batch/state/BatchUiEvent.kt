package com.workfort.pstuian.ui.faculty.batch.state

import com.workfort.pstuian.featuredomain.model.Batch

sealed interface BatchUiEvent {
    data object Refresh : BatchUiEvent
    data object LoadMore : BatchUiEvent
    data class BatchClicked(val batch: Batch) : BatchUiEvent
}
