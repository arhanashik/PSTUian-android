package com.workfort.pstuian.app.data.local.courseschedule

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Entity(tableName = TableNames.COURSE_SCHEDULE, indices = [Index(value = [ColumnNames.CourseSchedule.COURSE_CODE], unique = true)])
data class CourseScheduleEntity (@PrimaryKey(autoGenerate = true) val sl: Int,
                                 @ColumnInfo(name = ColumnNames.CourseSchedule.ID) val id: String?,
                                 @ColumnInfo(name = ColumnNames.CourseSchedule.COURSE_CODE)
                                 @SerializedName("course_code")
                                 val courseCode: String?,
                                 @ColumnInfo(name = ColumnNames.CourseSchedule.COURSE_TITLE)
                                 @SerializedName("course_title")
                                 val courseTitle: String?,
                                 @ColumnInfo(name = ColumnNames.CourseSchedule.CREDIT_HOUR)
                                 @SerializedName("credit_hour")
                                 val creditHour: String?,
                                 @ColumnInfo(name = ColumnNames.CourseSchedule.FACULTY) val faculty: String?,
                                 @ColumnInfo(name = ColumnNames.CourseSchedule.STATUS) val status: Int) : Parcelable {
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
        parcel.writeString(courseCode)
        parcel.writeString(courseTitle)
        parcel.writeString(creditHour)
        parcel.writeString(faculty)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CourseScheduleEntity> {
        override fun createFromParcel(parcel: Parcel): CourseScheduleEntity {
            return CourseScheduleEntity(parcel)
        }

        override fun newArray(size: Int): Array<CourseScheduleEntity?> {
            return arrayOfNulls(size)
        }
    }
}