package com.workfort.pstuian.app.ui.emailverification

import com.workfort.pstuian.view.service.StateReducer


class EmailVerificationScreenStateReducer : StateReducer<EmailVerificationScreenState, EmailVerificationScreenStateUpdate> {
 override val initial: EmailVerificationScreenState
  get() = EmailVerificationScreenState()
}