package com.workfort.pstuian.util.lib.koin

import com.workfort.pstuian.util.remote.RetrofitBuilder
import org.koin.dsl.module

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 9:09 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

// DI module
val networkModule = module {
    single { RetrofitBuilder.createAuthApiService() }
    single { RetrofitBuilder.createSliderApiService() }
    single { RetrofitBuilder.createFacultyApiService() }
    single { RetrofitBuilder.createStudentApiService() }
    single { RetrofitBuilder.createTeacherApiService() }
    single { RetrofitBuilder.createDonationApiService() }
    single { RetrofitBuilder.createFileHandlerApiService() }
    single { RetrofitBuilder.createSupportApiService() }
    single { RetrofitBuilder.createNotificationApiService() }
}