package com.workfort.pstuian.app.ui.signin

import com.workfort.pstuian.view.service.StateReducer


class SignInScreenStateReducer : StateReducer<SignInScreenState, SignInScreenStateUpdate> {
 override val initial: SignInScreenState
  get() = SignInScreenState()
}