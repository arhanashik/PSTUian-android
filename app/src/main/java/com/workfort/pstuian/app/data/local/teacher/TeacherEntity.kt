package com.workfort.pstuian.app.data.local.teacher

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Entity(tableName = TableNames.TEACHER, indices = [Index(value = [ColumnNames.Teacher.ID], unique = true)])
data class TeacherEntity(@PrimaryKey(autoGenerate = true) val sl: Int,
                         @ColumnInfo(name = ColumnNames.Teacher.ID) val id: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.NAME) val name: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.DESIGNATION)
                         val designation: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.STATUS) val status: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.PHONE) val phone: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.LINKED_IN)
                         @SerializedName("linked_in")
                         val linkedIn: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.FB_LINK)
                         @SerializedName("fb_link")
                         val fbLink: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.ADDRESS) val address: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.EMAIL) val email: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.DEPARTMENT) val department: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.FACULTY) val faculty: String?,
                         @ColumnInfo(name = ColumnNames.Teacher.IMAGE_URL)
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
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sl)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(designation)
        parcel.writeString(status)
        parcel.writeString(phone)
        parcel.writeString(linkedIn)
        parcel.writeString(fbLink)
        parcel.writeString(address)
        parcel.writeString(email)
        parcel.writeString(department)
        parcel.writeString(faculty)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TeacherEntity> {
        override fun createFromParcel(parcel: Parcel): TeacherEntity {
            return TeacherEntity(parcel)
        }

        override fun newArray(size: Int): Array<TeacherEntity?> {
            return arrayOfNulls(size)
        }
    }
}