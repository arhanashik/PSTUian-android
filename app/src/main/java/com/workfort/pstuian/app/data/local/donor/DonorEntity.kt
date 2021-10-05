package com.workfort.pstuian.app.data.local.donor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DonorEntity (
    val id: Int,
    val name: String?,
    val info: String?,
    val email: String?,
    val reference: String
) : Parcelable