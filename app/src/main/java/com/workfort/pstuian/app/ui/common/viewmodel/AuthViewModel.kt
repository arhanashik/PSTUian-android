package com.workfort.pstuian.app.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.AuthRepository
import com.workfort.pstuian.app.ui.home.viewstate.SignInUserState
import com.workfort.pstuian.app.ui.signin.viewstate.SignInState
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.signup.viewstate.SignOutState
import com.workfort.pstuian.app.ui.signup.viewstate.SignUpState
import com.workfort.pstuian.app.ui.splash.viewstate.ConfigState
import com.workfort.pstuian.util.helper.CoilUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {
    val intent = Channel<AuthIntent>(Channel.UNLIMITED)

    private val _configState = MutableStateFlow<ConfigState>(ConfigState.Idle)
    val configState: StateFlow<ConfigState> get() = _configState

    private val _signInUserState = MutableStateFlow<SignInUserState>(SignInUserState.Idle)
    val signInUserState: StateFlow<SignInUserState> get() = _signInUserState

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState: StateFlow<SignInState> get() = _signInState

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    private val _signOutState = MutableStateFlow<SignOutState>(SignOutState.Idle)
    val signOutState: StateFlow<SignOutState> get() = _signOutState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is AuthIntent.GetConfig -> getConfig()
                    is AuthIntent.GetSignInUser -> getSignedInUser()
                    is AuthIntent.SignOut -> signOut()
                }
            }
        }
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
                SignInUserState.User(authRepo.getSignedInUserFlow().first())
            } catch (e: Exception) {
                SignInUserState.Error(e.message)
            }
        }
    }

    fun signIn(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _signInState.value = SignInState.Loading
            _signInState.value = try {
                val response = authRepo.signIn(email, password, "student")
                SignInState.Success(response)
            } catch (e: Exception) {
                SignInState.Error(e.message)
            }
        }
    }

    fun signUp(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String
    ) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            _signUpState.value = try {
                val response = authRepo.signUp(name, id, reg, facultyId, batchId, session)
                SignUpState.Success(response)
            } catch (e: Exception) {
                SignUpState.Error(e.message)
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _signOutState.value = SignOutState.Loading
            _signOutState.value = try {
                val student = authRepo.getSignedInUserFlow().first()
                val response = authRepo.signOut(student.id, "student")
                CoilUtil.clearCache()
                SignOutState.Success(response)
            } catch (e: Exception) {
                SignOutState.Error(e.message)
            }
        }
    }
}
