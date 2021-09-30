package com.workfort.pstuian.app.data.local.student

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Entity(tableName = TableNames.STUDENT, indices = [Index(value = [ColumnNames.Student.ID], unique = true)])
data class StudentEntity(@PrimaryKey(autoGenerate = true) val sl: Int,
                         @ColumnInfo(name = ColumnNames.Student.NAME) val name: String?,
                         @ColumnInfo(name = ColumnNames.Student.ID) val id: String?,
                         @ColumnInfo(name = ColumnNames.Student.REG) val reg: String?,
                         @ColumnInfo(name = ColumnNames.Student.PHONE) val phone: String?,
                         @ColumnInfo(name = ColumnNames.Student.LINKED_IN)
                         @SerializedName("linked_in")
                         val linkedIn: String?,
                         @ColumnInfo(name = ColumnNames.Student.FB_LINK)
                         @SerializedName("fb_link")
                         val fbLink: String?,
                         @ColumnInfo(name = ColumnNames.Student.BLOOD) val blood: String?,
                         @ColumnInfo(name = ColumnNames.Student.ADDRESS) val address: String?,
                         @ColumnInfo(name = ColumnNames.Student.EMAIL) val email: String?,
                         @ColumnInfo(name = ColumnNames.Student.BATCH) val batch: String?,
                         @ColumnInfo(name = ColumnNames.Student.SESSION) val session: String?,
                         @ColumnInfo(name = ColumnNames.Student.FACULTY) val faculty: String?,
                         @ColumnInfo(name = ColumnNames.Student.IMAGE_URL)
                         @SerializedName("image_url")
                         val imageUrl: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sl)
        parcel.writeString(name)
        parcel.writeString(id)
        parcel.writeString(reg)
        parcel.writeString(phone)
        parcel.writeString(linkedIn)
        parcel.writeString(fbLink)
        parcel.writeString(blood)
        parcel.writeString(address)
        parcel.writeString(email)
        parcel.writeString(batch)
        parcel.writeString(session)
        parcel.writeString(faculty)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentEntity> {
        override fun createFromParcel(parcel: Parcel): StudentEntity {
            return StudentEntity(parcel)
        }

        override fun newArray(size: Int): Array<StudentEntity?> {
            return arrayOfNulls(size)
        }
    }
}