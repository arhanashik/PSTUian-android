package com.workfort.pstuian.app.ui.teacherprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.repository.TeacherRepository
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
 *  ****************************************************************************
 */

class TeacherProfileViewModel(private val teacherRepo: TeacherRepository) : ViewModel() {
    private val _getProfileState = MutableStateFlow<RequestState>(RequestState.Idle)
    val getProfileState: StateFlow<RequestState> get() = _getProfileState

    private val _changeProfileImageState = MutableStateFlow<RequestState>(RequestState.Idle)
    val changeProfileImageState: StateFlow<RequestState> get() = _changeProfileImageState

    private val _changeNameState = MutableStateFlow<RequestState>(RequestState.Idle)
    val changeNameState: StateFlow<RequestState> get() = _changeNameState

    private val _changeBioState = MutableStateFlow<RequestState>(RequestState.Idle)
    val changeBioState: StateFlow<RequestState> get() = _changeBioState

    private val _changeAcademicInfoState = MutableStateFlow<RequestState>(RequestState.Idle)
    val changeAcademicInfoState: StateFlow<RequestState> get() = _changeAcademicInfoState

    private val _changeConnectInfoState = MutableStateFlow<RequestState>(RequestState.Idle)
    val changeConnectInfoState: StateFlow<RequestState> get() = _changeConnectInfoState

    fun getProfile(teacherId: Int) {
        viewModelScope.launch {
            _getProfileState.value = RequestState.Loading
            _getProfileState.value = try {
                RequestState.Success(teacherRepo.getProfile(teacherId))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    fun changeProfileImage(teacher: TeacherEntity, imageUrl: String) {
        viewModelScope.launch {
            _changeProfileImageState.value = RequestState.Loading
            _changeProfileImageState.value = try {
                teacherRepo.changeProfileImage(teacher, imageUrl)
                RequestState.Success(imageUrl)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    fun changeName(
        teacher: TeacherEntity,
        newName: String
    ) {
        viewModelScope.launch {
            _changeNameState.value = RequestState.Loading
            _changeNameState.value = try {
                teacherRepo.changeName(teacher, newName)
                RequestState.Success(newName)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    fun changeBio(
        teacher: TeacherEntity,
        newBio: String
    ) {
        viewModelScope.launch {
            _changeBioState.value = RequestState.Loading
            _changeBioState.value = try {
                teacherRepo.changeBio(teacher, newBio)
                RequestState.Success(newBio)
            } catch (e: Exception) {
                RequestState.Error(e.message)
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
            _changeAcademicInfoState.value = RequestState.Loading
            _changeAcademicInfoState.value = try {
                val newTeacher = teacherRepo.changeAcademicInfo(teacher, name, designation,
                    department, blood, facultyId)
                RequestState.Success(newTeacher)
            } catch (e: Exception) {
                RequestState.Error(e.message)
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
            _changeConnectInfoState.value = RequestState.Loading
            _changeConnectInfoState.value = try {
                val newTeacher = teacherRepo.changeConnectInfo(teacher,
                    address, phone, email, linkedIn, fbLink)
                RequestState.Success(newTeacher)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }
}