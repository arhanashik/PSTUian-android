package com.workfort.pstuian.ui.faculty.batch

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.faculty.batch.state.BatchNavigationState
import com.workfort.pstuian.ui.faculty.batch.state.BatchUiEvent
import com.workfort.pstuian.ui.faculty.batch.state.BatchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BatchViewModel(
    private val facultyId: Int,
    private val facultyRepo: FacultyRepository,
    private val uiStateMachine: BatchUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<BatchUiState>(uiStateMachine) {

    private val _navigation = MutableStateFlow<BatchNavigationState?>(null)
    val navigation: StateFlow<BatchNavigationState?> = _navigation.asStateFlow()

    private val batchListCache = mutableListOf<Batch>()
    private var currentPage = 1
    private var hasMoreData = true

    override fun onUiReady() {
        loadData()
    }

    fun onUiEvent(event: BatchUiEvent) {
        when (event) {
            is BatchUiEvent.Refresh -> getBatches(forceRefresh = true)
            is BatchUiEvent.LoadMore -> getBatches(forceRefresh = false)
            is BatchUiEvent.BatchClicked -> onClickBatch(event.batch)
        }
    }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBatch(batch: Batch) {
        _navigation.update { BatchNavigationState.GoToStudents(batch.id) }
    }

    private fun loadData() {
        uiStateMachine.showInitialContent()
        getBatches(forceRefresh = false)
    }

    private fun getBatches(forceRefresh: Boolean) {
        if (forceRefresh) {
            batchListCache.clear()
            currentPage = 1
            hasMoreData = true
        } else if (!hasMoreData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showContentLoading(isLoading = true)
            facultyRepo.getBatches(facultyId, currentPage, forceRefresh)
                .onSuccess { batches ->
                    if (batches.isEmpty()) {
                        hasMoreData = false
                    } else {
                        currentPage++
                    }
                    batchListCache.addAll(batches)
                    uiStateMachine.showBatches(batchListCache.toList())
                }
                .onFailure {
                    uiStateMachine.showContentLoading(isLoading = false)
                    if (batchListCache.isEmpty()) {
                        val message = it.message ?: "Failed to load batches"
                        uiStateMachine.showError(message)
                    }
                }
        }
    }
}
