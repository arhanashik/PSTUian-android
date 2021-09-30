package com.workfort.pstuian.app.data.local.slider

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Entity(tableName = TableNames.SLIDER, indices = [Index(value = [ColumnNames.Slider.IMAGE_URL], unique = true)])
data class SliderEntity (@PrimaryKey(autoGenerate = true) val sl: Int,
                         @ColumnInfo(name = ColumnNames.Slider.ID) val id: String?,
                         @ColumnInfo(name = ColumnNames.Slider.TITLE) val title: String?,
                         @ColumnInfo(name = ColumnNames.Slider.IMAGE_URL)
                         @SerializedName("image_url")
                         val imageUrl: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sl)
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SliderEntity> {
        override fun createFromParcel(parcel: Parcel): SliderEntity {
            return SliderEntity(parcel)
        }

        override fun newArray(size: Int): Array<SliderEntity?> {
            return arrayOfNulls(size)
        }
    }
}