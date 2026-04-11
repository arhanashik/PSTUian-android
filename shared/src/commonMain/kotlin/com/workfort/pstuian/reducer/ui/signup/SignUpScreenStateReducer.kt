package com.workfort.pstuian.reducer.ui.signup

import com.workfort.pstuian.reducer.service.StateReducer


class SignUpScreenStateReducer : StateReducer<SignUpScreenState, SignUpScreenStateUpdate> {
    override val initial: SignUpScreenState
        get() = SignUpScreenState()
}
