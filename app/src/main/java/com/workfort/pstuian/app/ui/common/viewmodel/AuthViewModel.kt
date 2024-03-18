package com.workfort.pstuian.app.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.firebase.fcm.FcmUtil.getFcmToken
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.util.helper.CoilUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {
    val intent = Channel<AuthIntent>(Channel.UNLIMITED)

    var devicesPage = 1
    private val _devicesState = MutableStateFlow<RequestState>(RequestState.Idle)
    val devicesState: StateFlow<RequestState> get() = _devicesState

    private val _deviceRegistrationState = MutableStateFlow<RequestState>(RequestState.Idle)
    val deviceRegistrationState: StateFlow<RequestState> get() = _deviceRegistrationState

    private val _configState = MutableStateFlow<RequestState>(RequestState.Idle)
    val configState: StateFlow<RequestState> get() = _configState

    private val _signInUserState = MutableStateFlow<RequestState>(RequestState.Idle)
    val signInUserState: StateFlow<RequestState> get() = _signInUserState

    private val _signInState = MutableStateFlow<RequestState>(RequestState.Idle)
    val signInState: StateFlow<RequestState> get() = _signInState

    private val _studentSignUpState = MutableStateFlow<RequestState>(RequestState.Idle)
    val studentSignUpState: StateFlow<RequestState> get() = _studentSignUpState

    private val _teacherSignUpState = MutableStateFlow<RequestState>(RequestState.Idle)
    val teacherSignUpState: StateFlow<RequestState> get() = _teacherSignUpState

    private val _signOutState = MutableStateFlow<RequestState>(RequestState.Idle)
    val signOutState: StateFlow<RequestState> get() = _signOutState

    private val _changePasswordState = MutableStateFlow<RequestState>(RequestState.Idle)
    val changePasswordState: StateFlow<RequestState> get() = _changePasswordState

    private val _forgotPasswordState = MutableStateFlow<RequestState>(RequestState.Idle)
    val forgotPasswordState: StateFlow<RequestState> get() = _forgotPasswordState

    private val _emailVerificationState = MutableStateFlow<RequestState>(RequestState.Idle)
    val emailVerificationState: StateFlow<RequestState> get() = _emailVerificationState

    private val _accountDeleteState = MutableStateFlow<RequestState>(RequestState.Idle)
    val accountDeleteState: StateFlow<RequestState> get() = _accountDeleteState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is AuthIntent.GetAllDevices -> getAllDevices(it.page)
                    is AuthIntent.RegisterDevice -> registerDevice()
                    is AuthIntent.GetConfig -> getConfig()
                    is AuthIntent.GetSignInUser -> getSignedInUser()
                    is AuthIntent.SignUpStudent -> signUpStudent(
                        it.name, it.id, it.reg, it.facultyId, it.batchId, it.session, it.email
                    )
                    is AuthIntent.ChangePassword -> changePassword(it.oldPassword, it.newPassword)
                    is AuthIntent.EmailVerification -> emailVerification(it.userType, it.email)
                    is AuthIntent.SignOut -> signOut(it.fromAllDevices)
                    is AuthIntent.DeleteAccount -> deleteAccount(it.password)
                }
            }
        }
    }

    private fun getAllDevices(page: Int) {
        _devicesState.value = RequestState.Loading
        viewModelScope.launch {
            _devicesState.value = try {
                RequestState.Success(authRepo.getAllDevices(page))
            } catch (e: Exception) {
                RequestState.Error(e.message?: "Failed to get devices")
            }
        }
    }

    private fun registerDevice() {
        _deviceRegistrationState.value = RequestState.Loading
        getFcmToken { token, _ ->
            Timber.e("testR token $token")
            // Some device cannot generate fcm token(for example PocoPhone F1)
            // In that case, we should not make the app unusable.
            // We will register the app without the token.
//                if(error != null) {
//                    val errorMsg = "FCM Registration Failed: $error"
//                    _deviceRegistrationState.value = DeviceState.Error(errorMsg)
//                    return
//                }
            viewModelScope.launch {
                _deviceRegistrationState.value = try {
                    RequestState.Success(authRepo.registerDevice(token?: "Unknown"))
                } catch (e: Exception) {
                    RequestState.Error(e.message?: "Couldn't register the device")
                }
            }
        }
    }

    private fun getConfig() {
        viewModelScope.launch {
            _configState.value = RequestState.Loading
            _configState.value = try {
                RequestState.Success(authRepo.getConfig())
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun getSignedInUser() {
        viewModelScope.launch {
            _signInUserState.value = RequestState.Loading
            _signInUserState.value = try {
                RequestState.Success(authRepo.getSignInUser())
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    fun signIn(
        email: String,
        password: String,
        userType: String
    ) {
        viewModelScope.launch {
            _signInState.value = RequestState.Loading
            _signInState.value = try {
                val response = authRepo.signIn(email, password, userType)
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message)
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
            _studentSignUpState.value = RequestState.Loading
            _studentSignUpState.value = try {
                val response = authRepo.signUpStudent(name, id, reg, facultyId, batchId,
                    session, email)
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message)
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
            _teacherSignUpState.value = RequestState.Loading
            _teacherSignUpState.value = try {
                val response = authRepo.signUpTeacher(name, designation, department, email,
                    password, facultyId)
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun signOut(fromAllDevices: Boolean) {
        viewModelScope.launch {
            _signOutState.value = RequestState.Loading
            _signOutState.value = try {
                val response = authRepo.signOut(fromAllDevices)
                CoilUtil.clearCache()
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _changePasswordState.value = RequestState.Loading
            _changePasswordState.value = try {
                val response = authRepo.changePassword(oldPassword, newPassword)
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    fun forgotPassword(userType: String, email: String) {
        viewModelScope.launch {
            _forgotPasswordState.value = RequestState.Loading
            _forgotPasswordState.value = try {
                val response = authRepo.forgotPassword(userType, email)
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun emailVerification(userType: String, email: String) {
        viewModelScope.launch {
            _emailVerificationState.value = RequestState.Loading
            _emailVerificationState.value = try {
                RequestState.Success(authRepo.emailVerification(userType, email))
            } catch (e: Exception) {
                RequestState.Error(e.message ?: "Couldn't send verification email!")
            }
        }
    }

    private fun deleteAccount(password: String) {
        viewModelScope.launch {
            _accountDeleteState.value = RequestState.Loading
            _accountDeleteState.value = try {
                val response = authRepo.deleteAccount(password)
                CoilUtil.clearCache()
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }
}
