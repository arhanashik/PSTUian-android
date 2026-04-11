package com.workfort.pstuian.reducer.ui.splash

import com.workfort.pstuian.reducer.service.StateReducer


class SplashScreenStateReducer : StateReducer<SplashScreenState, SplashScreenStateUpdate> {
    override val initial: SplashScreenState
        get() = SplashScreenState()
}
