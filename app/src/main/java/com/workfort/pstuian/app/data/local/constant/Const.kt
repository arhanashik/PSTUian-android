package com.workfort.pstuian.app.data.local.constant

import com.workfort.pstuian.BuildConfig

object Const {
    object RequestCode {
        const val LOCATION = 111
        const val GOOGLE_SIGN_IN = 222
        const val CAPTURE_PHOTO = 333
        const val PICK_SINGLE_PHOTO = 444
        const val STORAGE_READ = 555
        const val STORAGE_WRITE = 666
        const val CREATE_POST = 777
        const val CREATE_EVENT = 888
        const val CREATE_INFO = 999
        const val CREATE_STORY = 1111
    }
    object Key{
        const val FACULTY = "FACULTY"
        const val BATCH = "BATCH"
    }

    object Remote {
        private const val DEV_SERVER_URL = "http://192.168.1.101:8888/PSTUian-web/api/mobile/v1/"
        private const val LIVE_SERVER_URL = ""
        val BASE_URL = if(BuildConfig.DEBUG) DEV_SERVER_URL else LIVE_SERVER_URL
    }
}