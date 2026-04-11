package com.workfort.pstuian.reducer.ui.signin

import com.workfort.pstuian.reducer.service.StateReducer


class SignInScreenStateReducer : StateReducer<SignInScreenState, SignInScreenStateUpdate> {
    override val initial: SignInScreenState
        get() = SignInScreenState()
}
