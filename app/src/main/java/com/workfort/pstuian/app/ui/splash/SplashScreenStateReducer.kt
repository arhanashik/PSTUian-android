package com.workfort.pstuian.app.ui.splash

import com.workfort.pstuian.view.service.StateReducer


class SplashScreenStateReducer : StateReducer<SplashScreenState, SplashScreenStateUpdate> {
 override val initial: SplashScreenState
  get() = SplashScreenState()
}