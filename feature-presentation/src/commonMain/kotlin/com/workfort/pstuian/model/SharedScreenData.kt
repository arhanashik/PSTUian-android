package com.workfort.pstuian.model

import com.workfort.pstuian.featuredomain.model.AppConfig
import com.workfort.pstuian.featuredomain.model.Device
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserType

class SharedScreenData {
    private var _currentUser: User? = null
    private var _appConfig: AppConfig? = null
    private var _device: Device? = null

    fun setCurrentUser(user: User?) {
        _currentUser = user
    }

    fun getCurrentUser() = _currentUser

    fun getCurrentUserType() = when (_currentUser) {
        is User.Student -> UserType.STUDENT
        is User.Teacher -> UserType.TEACHER
        is User.Employee -> UserType.EMPLOYEE
        else -> null
    }

    fun setAppConfig(config: AppConfig?) {
        _appConfig = config
    }

    fun getAppConfig() = _appConfig

    fun setDevice(device: Device?) {
        _device = device
    }

    fun getDevice() = _device
}