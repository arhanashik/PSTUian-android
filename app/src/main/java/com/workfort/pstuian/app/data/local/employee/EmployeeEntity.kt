package com.workfort.pstuian.app.data.local.employee

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Entity(tableName = TableNames.EMPLOYEE, indices = [Index(value = [ColumnNames.Employee.ID], unique = true)])
data class EmployeeEntity(@PrimaryKey(autoGenerate = true) val sl: Int,
                          @ColumnInfo(name = ColumnNames.Employee.ID) val id: String?,
                          @ColumnInfo(name = ColumnNames.Employee.NAME) val name: String?,
                          @ColumnInfo(name = ColumnNames.Employee.DESIGNATION)
                          val designation: String?,
                          @ColumnInfo(name = ColumnNames.Employee.DEPARTMENT) val department: String?,
                          @ColumnInfo(name = ColumnNames.Employee.PHONE) val phone: String?,
                          @ColumnInfo(name = ColumnNames.Employee.ADDRESS) val address: String?,
                          @ColumnInfo(name = ColumnNames.Employee.FACULTY) val faculty: String?,
                          @ColumnInfo(name = ColumnNames.Employee.IMAGE_URL)
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
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sl)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(designation)
        parcel.writeString(department)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(faculty)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EmployeeEntity> {
        override fun createFromParcel(parcel: Parcel): EmployeeEntity {
            return EmployeeEntity(parcel)
        }

        override fun newArray(size: Int): Array<EmployeeEntity?> {
            return arrayOfNulls(size)
        }
    }
}