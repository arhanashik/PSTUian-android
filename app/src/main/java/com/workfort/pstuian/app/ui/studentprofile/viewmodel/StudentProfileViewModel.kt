package com.workfort.pstuian.app.ui.studentprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.repository.StudentRepository
import com.workfort.pstuian.app.ui.studentprofile.intent.StudentProfileIntent
import com.workfort.pstuian.app.ui.studentprofile.viewstate.ChangeProfileInfoState
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
    private val studentRepo: StudentRepository
) : ViewModel() {
    val intent = Channel<StudentProfileIntent>(Channel.UNLIMITED)

    private val _changeProfileImageState = MutableStateFlow<ChangeProfileInfoState>(ChangeProfileInfoState.Idle)
    val changeProfileImageState: StateFlow<ChangeProfileInfoState> get() = _changeProfileImageState

    private val _changeNameState = MutableStateFlow<ChangeProfileInfoState>(ChangeProfileInfoState.Idle)
    val changeNameState: StateFlow<ChangeProfileInfoState> get() = _changeNameState

    fun changeProfileImage(
        student: StudentEntity,
        imageUrl: String
    ) {
        viewModelScope.launch {
            _changeProfileImageState.value = ChangeProfileInfoState.Loading
            _changeProfileImageState.value = try {
                studentRepo.changeProfileImage(student, imageUrl)
                ChangeProfileInfoState.Success
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }

    fun changeName(
        student: StudentEntity,
        newName: String
    ) {
        viewModelScope.launch {
            _changeNameState.value = ChangeProfileInfoState.Loading
            _changeNameState.value = try {
                studentRepo.changeName(student, newName)
                ChangeProfileInfoState.Success
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }
}