package com.workfort.pstuian.app.data.local.faculty

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Entity(tableName = TableNames.FACULTY, indices = [Index(value = [ColumnNames.Faculty.SHORT_TITLE], unique = true)])
data class FacultyEntity (@PrimaryKey(autoGenerate = true) val sl: Int,
                          @ColumnInfo(name = ColumnNames.Faculty.ID) val id: String?,
                          @ColumnInfo(name = ColumnNames.Faculty.SHORT_TITLE)
                          @SerializedName("short_title")
                          val shortTitle: String?,
                          @ColumnInfo(name = ColumnNames.Faculty.TITLE) val title: String?)
    : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sl)
        parcel.writeString(id)
        parcel.writeString(shortTitle)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FacultyEntity> {
        override fun createFromParcel(parcel: Parcel): FacultyEntity {
            return FacultyEntity(parcel)
        }

        override fun newArray(size: Int): Array<FacultyEntity?> {
            return arrayOfNulls(size)
        }
    }
}