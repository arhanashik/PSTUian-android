package com.workfort.pstuian.app.data.local.donor

import android.os.Parcel
import android.os.Parcelable

data class DonorEntity (val id: Int,
                        val name: String?,
                        val info: String?,
                        val email: String?,
                        val reference: String?)
    : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(info)
        parcel.writeString(email)
        parcel.writeString(reference)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DonorEntity> {
        override fun createFromParcel(parcel: Parcel): DonorEntity {
            return DonorEntity(parcel)
        }

        override fun newArray(size: Int): Array<DonorEntity?> {
            return arrayOfNulls(size)
        }
    }
}