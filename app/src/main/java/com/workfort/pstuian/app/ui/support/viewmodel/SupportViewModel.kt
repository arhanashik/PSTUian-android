package com.workfort.pstuian.app.ui.support.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.SupportRepository
import com.workfort.pstuian.app.ui.support.viewstate.InquiryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 11:23.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/28.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class SupportViewModel(private val repo: SupportRepository) : ViewModel() {
    private val _inquiryState = MutableStateFlow<InquiryState>(InquiryState.Idle)
    val inquiryState: StateFlow<InquiryState> get() = _inquiryState

    fun sendInquiry(name: String, email: String, query: String) {
        _inquiryState.value = InquiryState.Loading

        viewModelScope.launch {
            _inquiryState.value = try {
                val response = repo.sendInquiry(name, email, "query", query)
                InquiryState.Success(response)
            } catch (e: Exception) {
                InquiryState.Error(e.message)
            }
        }
    }
}