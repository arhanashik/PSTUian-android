package com.workfort.pstuian.app.ui.support.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.repository.SupportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SupportViewModel(private val repo: SupportRepository) : ViewModel() {
    private val _inquiryState = MutableStateFlow<RequestState>(RequestState.Idle)
    val inquiryState: StateFlow<RequestState> get() = _inquiryState

    fun sendInquiry(name: String, email: String, query: String) {
        _inquiryState.value = RequestState.Loading

        viewModelScope.launch {
            _inquiryState.value = try {
                val response = repo.sendInquiry(name, email, "query", query)
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }
}