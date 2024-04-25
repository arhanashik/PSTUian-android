package com.workfort.pstuian.app.ui.forgotpassword

import com.workfort.pstuian.view.service.StateReducer


class ForgotPasswordScreenStateReducer : StateReducer<ForgotPasswordScreenState, ForgotPasswordScreenStateUpdate> {
 override val initial: ForgotPasswordScreenState
  get() = ForgotPasswordScreenState()
}