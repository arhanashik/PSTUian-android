package com.workfort.pstuian.app.ui.studentprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.repository.StudentRepository
import com.workfort.pstuian.app.ui.studentprofile.intent.StudentProfileIntent
import com.workfort.pstuian.app.ui.studentprofile.viewstate.ChangeProfileInfoState
import com.workfort.pstuian.app.ui.studentprofile.viewstate.GetProfileState
import com.workfort.pstuian.util.helper.CoilUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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

    private val _getProfileState = MutableStateFlow<GetProfileState>(GetProfileState.Idle)
    val getProfileState: StateFlow<GetProfileState> get() = _getProfileState

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
            _getProfileState.value = GetProfileState.Loading
            _getProfileState.value = try {
                GetProfileState.Success(studentRepo.getProfile(studentId))
            } catch (e: Exception) {
                GetProfileState.Error(e.message)
            }
        }
    }

    private fun changeProfileImage(
        student: StudentEntity,
        imageUrl: String
    ) {
        viewModelScope.launch {
            _changeProfileImageState.value = ChangeProfileInfoState.Loading
            _changeProfileImageState.value = try {
                studentRepo.changeProfileImage(student, imageUrl)
                CoilUtil.clearUrlCache(imageUrl)
                CoilUtil.clearCache()
                ChangeProfileInfoState.Success(imageUrl)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }

    private fun changeName(
        student: StudentEntity,
        newName: String
    ) {
        viewModelScope.launch {
            _changeNameState.value = ChangeProfileInfoState.Loading
            _changeNameState.value = try {
                studentRepo.changeName(student, newName)
                ChangeProfileInfoState.Success(newName)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }

    private fun changeBio(
        student: StudentEntity,
        newBio: String
    ) {
        viewModelScope.launch {
            _changeBioState.value = ChangeProfileInfoState.Loading
            _changeBioState.value = try {
                studentRepo.changeBio(student, newBio)
                ChangeProfileInfoState.Success(newBio)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
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
            _changeAcademicInfoState.value = ChangeProfileInfoState.Loading
            _changeAcademicInfoState.value = try {
                val newStudent = studentRepo.changeAcademicInfo(student, name, id, reg, blood,
                    facultyId, session, batchId)
                ChangeProfileInfoState.Success(newStudent)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
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
            _changeConnectInfoState.value = ChangeProfileInfoState.Loading
            _changeConnectInfoState.value = try {
                val newStudent = studentRepo.changeConnectInfo(student,
                    address, phone, email, cvLink, linkedIn, facebook)
                ChangeProfileInfoState.Success(newStudent)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }
}