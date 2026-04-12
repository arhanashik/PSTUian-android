package com.workfort.pstuian.reducer.ui.emailverification

import com.workfort.pstuian.reducer.service.StateReducer


class EmailVerificationScreenStateReducer : StateReducer<EmailVerificationScreenState, EmailVerificationScreenStateUpdate> {
    override val initial: EmailVerificationScreenState
        get() = EmailVerificationScreenState()
}