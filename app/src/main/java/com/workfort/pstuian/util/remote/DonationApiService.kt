package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.app.data.remote.Response
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
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
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