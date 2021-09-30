package com.workfort.pstuian.app.data.local.batch

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Entity(tableName = TableNames.BATCH, indices = [Index(value = [ColumnNames.Batch.SESSION], unique = true)])
data class BatchEntity (@PrimaryKey(autoGenerate = true) val sl: Int,
                        @ColumnInfo(name = ColumnNames.Batch.ID) val id: String?,
                        @ColumnInfo(name = ColumnNames.Batch.NAME) val name: String?,
                        @ColumnInfo(name = ColumnNames.Batch.TITLE) val title: String?,
                        @ColumnInfo(name = ColumnNames.Batch.SESSION) val session: String?,
                        @ColumnInfo(name = ColumnNames.Batch.FACULTY) val faculty: String?,
                        @ColumnInfo(name = ColumnNames.Batch.TOTAL_STUDENT)
                        @SerializedName("total_student")
                        val totalStudent: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sl)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(title)
        parcel.writeString(session)
        parcel.writeString(faculty)
        parcel.writeInt(totalStudent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BatchEntity> {
        override fun createFromParcel(parcel: Parcel): BatchEntity {
            return BatchEntity(parcel)
        }

        override fun newArray(size: Int): Array<BatchEntity?> {
            return arrayOfNulls(size)
        }
    }
}