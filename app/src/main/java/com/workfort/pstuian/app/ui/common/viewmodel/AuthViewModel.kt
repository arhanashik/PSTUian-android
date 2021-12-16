package com.workfort.pstuian.app.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.AuthRepository
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.forgotpassword.viewstate.ForgotPasswordState
import com.workfort.pstuian.app.ui.home.viewstate.SignInUserState
import com.workfort.pstuian.app.ui.signin.viewstate.SignInState
import com.workfort.pstuian.app.ui.signup.viewstate.SignOutState
import com.workfort.pstuian.app.ui.signup.viewstate.StudentSignUpState
import com.workfort.pstuian.app.ui.signup.viewstate.TeacherSignUpState
import com.workfort.pstuian.app.ui.splash.viewstate.ConfigState
import com.workfort.pstuian.app.ui.splash.viewstate.DeviceRegistrationState
import com.workfort.pstuian.app.ui.studentprofile.viewstate.ChangeProfileInfoState
import com.workfort.pstuian.util.helper.CoilUtil
import com.workfort.pstuian.util.lib.fcm.FcmUtil
import com.workfort.pstuian.util.lib.fcm.callback.FcmTokenCallback
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {
    val intent = Channel<AuthIntent>(Channel.UNLIMITED)

    private val _deviceRegistrationState = MutableStateFlow<DeviceRegistrationState>(DeviceRegistrationState.Idle)
    val deviceRegistrationState: StateFlow<DeviceRegistrationState> get() = _deviceRegistrationState

    private val _configState = MutableStateFlow<ConfigState>(ConfigState.Idle)
    val configState: StateFlow<ConfigState> get() = _configState

    private val _signInUserState = MutableStateFlow<SignInUserState>(SignInUserState.Idle)
    val signInUserState: StateFlow<SignInUserState> get() = _signInUserState

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState: StateFlow<SignInState> get() = _signInState

    private val _studentSignUpState = MutableStateFlow<StudentSignUpState>(StudentSignUpState.Idle)
    val studentSignUpState: StateFlow<StudentSignUpState> get() = _studentSignUpState

    private val _teacherSignUpState = MutableStateFlow<TeacherSignUpState>(TeacherSignUpState.Idle)
    val teacherSignUpState: StateFlow<TeacherSignUpState> get() = _teacherSignUpState

    private val _signOutState = MutableStateFlow<SignOutState>(SignOutState.Idle)
    val signOutState: StateFlow<SignOutState> get() = _signOutState

    private val _changePasswordState = MutableStateFlow<ChangeProfileInfoState>(ChangeProfileInfoState.Idle)
    val changePasswordState: StateFlow<ChangeProfileInfoState> get() = _changePasswordState

    private val _forgotPasswordState = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState: StateFlow<ForgotPasswordState> get() = _forgotPasswordState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is AuthIntent.RegisterDevice -> registerDevice()
                    is AuthIntent.GetConfig -> getConfig()
                    is AuthIntent.GetSignInUser -> getSignedInUser()
                    is AuthIntent.SignUpStudent -> signUpStudent(
                        it.name, it.id, it.reg, it.facultyId, it.batchId, it.session, it.email
                    )
                    is AuthIntent.ChangePassword -> changePassword(it.oldPassword, it.newPassword)
                    is AuthIntent.SignOut -> signOut()
                }
            }
        }
    }

    private fun registerDevice() {
        _deviceRegistrationState.value = DeviceRegistrationState.Loading
        FcmUtil.getFcmToken(object: FcmTokenCallback {
            override fun onResponse(token: String?, error: String?) {
                if(error != null) {
                    _deviceRegistrationState.value = DeviceRegistrationState.Error(error)
                    return
                }
                viewModelScope.launch {
                    _deviceRegistrationState.value = try {
                        DeviceRegistrationState.Success(authRepo.registerDevice(token!!))
                    } catch (e: Exception) {
                        DeviceRegistrationState.Error(e.message)
                    }
                }
            }
        })
    }

    private fun getConfig() {
        viewModelScope.launch {
            _configState.value = ConfigState.Loading
            _configState.value = try {
                ConfigState.Success(authRepo.getConfig())
            } catch (e: Exception) {
                ConfigState.Error(e.message)
            }
        }
    }

    private fun getSignedInUser() {
        viewModelScope.launch {
            _signInUserState.value = SignInUserState.Loading
            _signInUserState.value = try {
                SignInUserState.User(authRepo.getSignInUser())
            } catch (e: Exception) {
                Timber.e(e)
                SignInUserState.Error(e.message)
            }
        }
    }

    fun signIn(
        email: String,
        password: String,
        userType: String
    ) {
        viewModelScope.launch {
            _signInState.value = SignInState.Loading
            _signInState.value = try {
                val response = authRepo.signIn(email, password, userType)
                SignInState.Success(response)
            } catch (e: Exception) {
                SignInState.Error(e.message)
            }
        }
    }

    private fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
    ) {
        viewModelScope.launch {
            _studentSignUpState.value = StudentSignUpState.Loading
            _studentSignUpState.value = try {
                val response = authRepo.signUpStudent(name, id, reg, facultyId, batchId,
                    session, email)
                StudentSignUpState.Success(response)
            } catch (e: Exception) {
                StudentSignUpState.Error(e.message)
            }
        }
    }

    fun signUpTeacher(
        name: String,
        designation: String,
        department: String,
        email: String,
        password: String,
        facultyId: Int,
    ) {
        viewModelScope.launch {
            _teacherSignUpState.value = TeacherSignUpState.Loading
            _teacherSignUpState.value = try {
                val response = authRepo.signUpTeacher(name, designation, department, email,
                    password, facultyId)
                TeacherSignUpState.Success(response)
            } catch (e: Exception) {
                TeacherSignUpState.Error(e.message)
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _signOutState.value = SignOutState.Loading
            _signOutState.value = try {
                val response = authRepo.signOut()
                CoilUtil.clearCache()
                SignOutState.Success(response)
            } catch (e: Exception) {
                SignOutState.Error(e.message)
            }
        }
    }

    private fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _changePasswordState.value = ChangeProfileInfoState.Loading
            _changePasswordState.value = try {
                val response = authRepo.changePassword(oldPassword, newPassword)
                ChangeProfileInfoState.Success(response)
            } catch (e: Exception) {
                ChangeProfileInfoState.Error(e.message)
            }
        }
    }

    fun forgotPassword(userType: String, email: String) {
        viewModelScope.launch {
            _forgotPasswordState.value = ForgotPasswordState.Loading
            _forgotPasswordState.value = try {
                val response = authRepo.forgotPassword(userType, email)
                ForgotPasswordState.Success(response)
            } catch (e: Exception) {
                ForgotPasswordState.Error(e.message)
            }
        }
    }
}
