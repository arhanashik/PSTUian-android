package com.workfort.pstuian.networking

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.networking.service.AuthApiService
import com.workfort.pstuian.networking.service.BloodDonationApiService
import com.workfort.pstuian.networking.service.BloodDonationRequestApiService
import com.workfort.pstuian.networking.service.CheckInApiService
import com.workfort.pstuian.networking.service.CheckInLocationApiService
import com.workfort.pstuian.networking.service.DonationApiService
import com.workfort.pstuian.networking.service.FacultyApiService
import com.workfort.pstuian.networking.service.FileHandlerApiService
import com.workfort.pstuian.networking.service.NotificationApiService
import com.workfort.pstuian.networking.service.SliderApiService
import com.workfort.pstuian.networking.service.StudentApiService
import com.workfort.pstuian.networking.service.SupportApiService
import com.workfort.pstuian.networking.service.TeacherApiService
import com.workfort.pstuian.sharedpref.Prefs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    private val blacklistLogs = arrayListOf(
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

    // create a logger
    private val logging = HttpLoggingInterceptor { message ->
        val isBlackListedLog = blacklistLogs.stream().anyMatch { message.startsWith(it) }
        if (isBlackListedLog.not()) {
            Timber.tag("OkHttp").e(message)
        }
    }.apply {
        setLevel(HttpLoggingInterceptor.Level.BASIC)
        redactHeader("Authorization")
        redactHeader("Cookie")
    }

    private val requestInterceptor = Interceptor { chain ->
        val authToken = Prefs.authToken ?: ""
        val request = chain.request().newBuilder()
            .header("Cache-Control", "no-cache")
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
        NetworkConst.Remote.BASE_API_URL,
    ).create(NotificationApiService::class.java)

    // blood donation api service
    fun createBloodDonationApiService(): BloodDonationApiService = retrofit(
        NetworkConst.Remote.BASE_API_URL,
    ).create(BloodDonationApiService::class.java)

    // blood donation request api service
    fun createBloodDonationRequestApiService(): BloodDonationRequestApiService = retrofit(
        NetworkConst.Remote.BASE_API_URL,
    ).create(BloodDonationRequestApiService::class.java)

    // check in api service
    fun createCheckInApiService(): CheckInApiService = retrofit(NetworkConst.Remote.BASE_API_URL)
        .create(CheckInApiService::class.java)

    // check in location api service
    fun createCheckInLocationApiService(): CheckInLocationApiService = retrofit(
        NetworkConst.Remote.BASE_API_URL,
    ).create(CheckInLocationApiService::class.java)
}