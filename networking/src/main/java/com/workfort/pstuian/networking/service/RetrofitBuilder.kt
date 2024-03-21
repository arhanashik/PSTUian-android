package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.networking.BuildConfig
import com.workfort.pstuian.sharedpref.Prefs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

object RetrofitBuilder {
    // create a logger
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

    private val requestInterceptor = Interceptor { chain ->
        val authToken = Prefs.authToken?: ""
        val request =
            chain.request().newBuilder()
                .header("x-auth-token", authToken)
                .build()
        chain.proceed(request)
    }

    // build an OkHttp client
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
    fun createAuthApiService(): AuthApiService = retrofit(NetworkConst.Remote.BASE_API_URL)
        .create(AuthApiService::class.java)

    // slider api service
    fun createSliderApiService(): SliderApiService = retrofit(NetworkConst.Remote.BASE_API_URL)
        .create(SliderApiService::class.java)

    // faculty api service
    fun createFacultyApiService(): FacultyApiService = retrofit(NetworkConst.Remote.BASE_API_URL)
        .create(FacultyApiService::class.java)

    // student api service
    fun createStudentApiService(): StudentApiService = retrofit(NetworkConst.Remote.BASE_API_URL)
        .create(StudentApiService::class.java)

    // teacher api service
    fun createTeacherApiService(): TeacherApiService = retrofit(NetworkConst.Remote.BASE_API_URL)
        .create(TeacherApiService::class.java)

    // donation api service
    fun createDonationApiService(): DonationApiService = retrofit(NetworkConst.Remote.BASE_API_URL)
        .create(DonationApiService::class.java)

    // file uploader api service
    fun createFileHandlerApiService(): FileHandlerApiService =
        retrofit(NetworkConst.Remote.BASE_API_URL).create(FileHandlerApiService::class.java)

    // support api service
    fun createSupportApiService(): SupportApiService = retrofit(NetworkConst.Remote.BASE_API_URL)
        .create(SupportApiService::class.java)

    // notification api service
    fun createNotificationApiService(): NotificationApiService = retrofit(
        NetworkConst.Remote.BASE_API_URL
    ).create(NotificationApiService::class.java)

    // blood donation api service
    fun createBloodDonationApiService(): BloodDonationApiService = retrofit(
        NetworkConst.Remote.BASE_API_URL
    ).create(BloodDonationApiService::class.java)

    // blood donation request api service
    fun createBloodDonationRequestApiService(): BloodDonationRequestApiService = retrofit(
        NetworkConst.Remote.BASE_API_URL
    ).create(BloodDonationRequestApiService::class.java)

    // check in api service
    fun createCheckInApiService(): CheckInApiService = retrofit(NetworkConst.Remote.BASE_API_URL)
        .create(CheckInApiService::class.java)

    // check in location api service
    fun createCheckInLocationApiService(): CheckInLocationApiService = retrofit(
        NetworkConst.Remote.BASE_API_URL
    ).create(CheckInLocationApiService::class.java)
}