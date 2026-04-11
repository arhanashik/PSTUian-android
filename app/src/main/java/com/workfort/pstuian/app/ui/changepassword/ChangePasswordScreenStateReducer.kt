package com.workfort.pstuian.app.ui.changepassword

import com.workfort.pstuian.reducer.service.StateReducer


class ChangePasswordScreenStateReducer : StateReducer<ChangePasswordScreenState, ChangePasswordScreenStateUpdate> {
 override val initial: ChangePasswordScreenState
  get() = ChangePasswordScreenState()
}