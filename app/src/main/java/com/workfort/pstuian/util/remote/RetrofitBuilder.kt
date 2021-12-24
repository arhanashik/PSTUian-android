package com.workfort.pstuian.util.remote

import com.workfort.pstuian.BuildConfig
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.pref.Prefs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 9:45 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

object RetrofitBuilder {
    //create a logger
    private val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            val blacklist = arrayOf(
                "Access-Control",
                "Cache-Control",
                "Connection",
                "Content-Type",
                "Keep-Alive",
                "Pragma",
                "Server",
                "Vary",
                "X-Powered-By"
            )
            for (bString in blacklist) {
                if (message.startsWith(bString)) return
            }
            Timber.tag("OkHttp").e(message)
        }
    }).apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
        redactHeader("Authorization")
        redactHeader("Cookie")
    }

    private val requestInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val authToken = Prefs.authToken?: ""
            val request =
                chain.request().newBuilder()
                    .header("x-auth-token", authToken)
                    .build()
            return chain.proceed(request)
        }
    }

    //build an OkHttp client
    private val client = OkHttpClient
        .Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .apply {
            if(BuildConfig.DEBUG){
                addInterceptor(logging)
            }
        }
        .addInterceptor(requestInterceptor)
        .addInterceptor(logging)
        .build()

    //create retrofit instance
    private fun retrofit(baseUrl: String) = Retrofit.Builder()
        .client(client)
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    //auth api service
    fun createAuthApiService(): AuthApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(AuthApiService::class.java)

    // slider api service
    fun createSliderApiService(): SliderApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(SliderApiService::class.java)

    // faculty api service
    fun createFacultyApiService(): FacultyApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(FacultyApiService::class.java)

    // student api service
    fun createStudentApiService(): StudentApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(StudentApiService::class.java)

    // teacher api service
    fun createTeacherApiService(): TeacherApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(TeacherApiService::class.java)

    // donation api service
    fun createDonationApiService(): DonationApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(DonationApiService::class.java)

    // file uploader api service
    fun createFileHandlerApiService(): FileHandlerApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(FileHandlerApiService::class.java)

    // support api service
    fun createSupportApiService(): SupportApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(SupportApiService::class.java)

    // notification api service
    fun createNotificationApiService(): NotificationApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(NotificationApiService::class.java)

    // blood donation api service
    fun createBloodDonationApiService(): BloodDonationApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(BloodDonationApiService::class.java)

    // blood donation request api service
    fun createBloodDonationRequestApiService(): BloodDonationRequestApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(BloodDonationRequestApiService::class.java)

    // check in api service
    fun createCheckInApiService(): CheckInApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(CheckInApiService::class.java)

    // check in location api service
    fun createCheckInLocationApiService(): CheckInLocationApiService = retrofit(Const.Remote.BASE_API_URL)
        .create(CheckInLocationApiService::class.java)
}