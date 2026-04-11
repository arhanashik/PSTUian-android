package com.workfort.pstuian.app.ui.forgotpassword

import com.workfort.pstuian.reducer.service.StateReducer


class ForgotPasswordScreenStateReducer : StateReducer<ForgotPasswordScreenState, ForgotPasswordScreenStateUpdate> {
 override val initial: ForgotPasswordScreenState
  get() = ForgotPasswordScreenState()
}