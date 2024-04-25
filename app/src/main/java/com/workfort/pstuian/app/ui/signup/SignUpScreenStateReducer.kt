package com.workfort.pstuian.app.ui.signup

import com.workfort.pstuian.view.service.StateReducer


class SignUpScreenStateReducer : StateReducer<SignUpScreenState, SignUpScreenStateUpdate> {
 override val initial: SignUpScreenState
  get() = SignUpScreenState()
}