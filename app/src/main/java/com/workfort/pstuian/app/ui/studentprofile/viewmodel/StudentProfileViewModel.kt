package com.workfort.pstuian.app.ui.studentprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.StudentRepository
import com.workfort.pstuian.app.ui.studentprofile.intent.StudentProfileIntent
import com.workfort.pstuian.app.ui.studentprofile.viewstate.ChangeProfileImageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 *  ****************************************************************************
 *  * Created by : arhan on 20 Oct, 2021 at 4:06 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/20/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class StudentProfileViewModel(
    private val repository: StudentRepository
) : ViewModel() {
    val intent = Channel<StudentProfileIntent>(Channel.UNLIMITED)

    private val _changeProfileImageState = MutableStateFlow<ChangeProfileImageState>(ChangeProfileImageState.Idle)
    val changeProfileImageState: StateFlow<ChangeProfileImageState> get() = _changeProfileImageState

    fun changeProfileImage(
        id: Int,
        imageUrl: String
    ) {
        viewModelScope.launch {
            _changeProfileImageState.value = ChangeProfileImageState.Loading
            _changeProfileImageState.value = try {
                val response = repository.changeProfileImage(id, imageUrl)
                ChangeProfileImageState.Success(response)
            } catch (e: Exception) {
                ChangeProfileImageState.Error(e.message)
            }
        }
    }
}