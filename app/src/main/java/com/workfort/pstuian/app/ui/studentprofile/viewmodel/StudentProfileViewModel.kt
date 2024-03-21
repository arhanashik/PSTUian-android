package com.workfort.pstuian.app.ui.studentprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.studentprofile.intent.StudentProfileIntent
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.repository.StudentRepository
import com.workfort.pstuian.util.helper.CoilUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
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
 *  ****************************************************************************
 */

class StudentProfileViewModel(private val studentRepo: StudentRepository) : ViewModel() {
    val intent = Channel<StudentProfileIntent>(Channel.UNLIMITED)

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

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when(it) {
                    is StudentProfileIntent.GetProfile -> getProfile(it.id)
                    is StudentProfileIntent.ChangeProfileImage ->
                        changeProfileImage(it.student, it.imageUrl)
                    is StudentProfileIntent.ChangeName -> changeName(it.student, it.newName)
                    is StudentProfileIntent.ChangeBio -> changeBio(it.student, it.newBio)
                    is StudentProfileIntent.ChangeAcademicInfo -> changeAcademicInfo(it.student,
                        it.name, it.id, it.reg, it.blood, it.facultyId, it.session, it.batchId)
                    is StudentProfileIntent.ChangeConnectInfo -> changeConnectInfo(it.student,
                        it.address, it.phone, it.email, it.cvLink, it.linkedIn, it.facebook)
                }
            }
        }
    }

    private fun getProfile(studentId: Int) {
        viewModelScope.launch {
            _getProfileState.value = RequestState.Loading
            _getProfileState.value = try {
                RequestState.Success(studentRepo.getProfile(studentId))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun changeProfileImage(student: StudentEntity, imageUrl: String) {
        viewModelScope.launch {
            _changeProfileImageState.value = RequestState.Loading
            _changeProfileImageState.value = try {
                studentRepo.changeProfileImage(student, imageUrl)
                CoilUtil.clearUrlCache(imageUrl)
                CoilUtil.clearCache()
                RequestState.Success(imageUrl)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun changeName(
        student: StudentEntity,
        newName: String
    ) {
        viewModelScope.launch {
            _changeNameState.value = RequestState.Loading
            _changeNameState.value = try {
                studentRepo.changeName(student, newName)
                RequestState.Success(newName)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun changeBio(
        student: StudentEntity,
        newBio: String
    ) {
        viewModelScope.launch {
            _changeBioState.value = RequestState.Loading
            _changeBioState.value = try {
                studentRepo.changeBio(student, newBio)
                RequestState.Success(newBio)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun changeAcademicInfo(
        student: StudentEntity,
        name: String,
        id: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int
    ) {
        viewModelScope.launch {
            _changeAcademicInfoState.value = RequestState.Loading
            _changeAcademicInfoState.value = try {
                val newStudent = studentRepo.changeAcademicInfo(student, name, id, reg, blood,
                    facultyId, session, batchId)
                RequestState.Success(newStudent)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun changeConnectInfo(
        student: StudentEntity,
        address: String,
        phone: String,
        email: String,
        cvLink: String,
        linkedIn: String,
        facebook: String
    ) {
        viewModelScope.launch {
            _changeConnectInfoState.value = RequestState.Loading
            _changeConnectInfoState.value = try {
                val newStudent = studentRepo.changeConnectInfo(student,
                    address, phone, email, cvLink, linkedIn, facebook)
                RequestState.Success(newStudent)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }
}