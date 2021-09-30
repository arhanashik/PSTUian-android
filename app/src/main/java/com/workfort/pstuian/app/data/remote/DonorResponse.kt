package com.workfort.pstuian.app.data.remote

import com.workfort.pstuian.app.data.local.donor.DonorEntity

data class DonorResponse (val success: Boolean,
                          val message: String,
                          val donors: ArrayList<DonorEntity>)