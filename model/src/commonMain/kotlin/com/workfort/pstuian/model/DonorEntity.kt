package com.workfort.pstuian.model

@Parcelize
data class DonorEntity (
    val id: Int,
    val name: String?,
    val info: String?,
    val email: String?,
    val reference: String
) : Parcelable
