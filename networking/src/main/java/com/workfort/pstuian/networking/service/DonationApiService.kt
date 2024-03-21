package com.workfort.pstuian.networking.service

import com.workfort.pstuian.model.DonorEntity
import com.workfort.pstuian.model.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:38 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

interface DonationApiService {
    @GET("donation.php?call=option")
    suspend fun getDonationOption(): Response<String>

    @FormUrlEncoded
    @POST("donation.php?call=save")
    suspend fun saveDonation(@Field("name") name: String,
                     @Field("info") info: String,
                     @Field("email") email: String,
                     @Field("reference") reference: String)
            : Response<Int>

    @GET("donation.php?call=donors")
    suspend fun getDonors(): Response<List<DonorEntity>>
}