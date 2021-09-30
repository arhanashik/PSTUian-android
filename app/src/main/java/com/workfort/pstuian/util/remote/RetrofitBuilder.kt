package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.constant.Const
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

    //build an OkHttp client
    private val client = OkHttpClient
        .Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    //create retrofit instance
    private fun retrofit(baseUrl: String) = Retrofit.Builder()
        .client(client)
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    //slider api service
    fun createSliderApiService(): SliderApiService = retrofit(Const.Remote.BASE_URL)
        .create(SliderApiService::class.java)

    //faculty api service
    fun createFacultyApiService(): FacultyApiService = retrofit(Const.Remote.BASE_URL)
        .create(FacultyApiService::class.java)

    //donation api service
    fun createDonationApiService(): DonationApiService = retrofit(Const.Remote.BASE_URL)
        .create(DonationApiService::class.java)
}