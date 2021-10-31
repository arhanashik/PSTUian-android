package com.workfort.pstuian.app.ui.teacherprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.data.repository.TeacherRepository
import com.workfort.pstuian.app.ui.studentprofile.viewstate.ChangeProfileInfoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Nov, 2021 at 0:59 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 11/01/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class TeacherProfileViewModel(
    private val teacherRepo: TeacherRepository
) : ViewModel() {
    private val _changeProfileImageState = MutableStateFlow<ChangeProfileInfoState>(ChangeProfileInfoState.Idle)
    val changeProfileImageState: StateFlow<ChangeProfileInfoState> get() = _changeProfileImageState

    private val _changeNameState = MutableStateFlow<ChangeProfileInfoState>(ChangeProfileInfoState.Idle)
    val changeNameState: StateFlow<ChangeProfileInfoState> get() = _changeNameState

    private val _changeBioState = MutableStateFlow<ChangeProfileInfoState>(ChangeProfileInfoState.Idle)
    val changeBioState: StateFlow<ChangeProfileInfoState> get() = _changeBioState

    private val _changeAcademicInfoState = MutableStateFlow<ChangeProfileInfoState>(ChangeProfileInfoState.Idle)
    val changeAcademicInfoState: StateFlow<ChangeProfileInfoState> get() = _changeAcademicInfoState

    private val _changeConnectInfoState = MutableStateFlow<ChangeProfileInfoState>(ChangeProfileInfoState.Idle)
    val changeConnectInfoState: StateFlow<ChangeProfileInfoState> get() = _changeConnectInfoState

    fun changeProfileImage(
        teacher: TeacherEntity,
        imageUrl: String
    ) {
        viewModelScope.launch {
            _changeProfileImageState.value = ChangeProfileInfoState.Loading
            _changeProfileImageState.value = try {
                teacherRepo.changeProfileImage(teacher, imageUrl)
                ChangeProfileInfoState.Success(imageUrl)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }

    fun changeName(
        teacher: TeacherEntity,
        newName: String
    ) {
        viewModelScope.launch {
            _changeNameState.value = ChangeProfileInfoState.Loading
            _changeNameState.value = try {
                teacherRepo.changeName(teacher, newName)
                ChangeProfileInfoState.Success(newName)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }

    fun changeBio(
        teacher: TeacherEntity,
        newBio: String
    ) {
        viewModelScope.launch {
            _changeBioState.value = ChangeProfileInfoState.Loading
            _changeBioState.value = try {
                teacherRepo.changeBio(teacher, newBio)
                ChangeProfileInfoState.Success(newBio)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }

    fun changeAcademicInfo(
        teacher: TeacherEntity,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ) {
        viewModelScope.launch {
            _changeAcademicInfoState.value = ChangeProfileInfoState.Loading
            _changeAcademicInfoState.value = try {
                val newTeacher = teacherRepo.changeAcademicInfo(teacher, name, designation,
                    department, blood, facultyId)
                ChangeProfileInfoState.Success(newTeacher)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }

    fun changeConnectInfo(
        teacher: TeacherEntity,
        address: String,
        phone: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ) {
        viewModelScope.launch {
            _changeConnectInfoState.value = ChangeProfileInfoState.Loading
            _changeConnectInfoState.value = try {
                val newTeacher = teacherRepo.changeConnectInfo(teacher,
                    address, phone, email, linkedIn, fbLink)
                ChangeProfileInfoState.Success(newTeacher)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }
}