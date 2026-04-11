package com.workfort.pstuian.reducer.ui.forgotpassword

import com.workfort.pstuian.reducer.service.StateReducer


class ForgotPasswordScreenStateReducer : StateReducer<ForgotPasswordScreenState, ForgotPasswordScreenStateUpdate> {
 override val initial: ForgotPasswordScreenState
  get() = ForgotPasswordScreenState()
}