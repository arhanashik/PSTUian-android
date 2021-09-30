package com.workfort.pstuian.app.data.local.slider

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TableNames.SLIDER)
data class SliderEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Slider.TITLE) val title: String?,
    @ColumnInfo(name = ColumnNames.Slider.IMAGE_URL)
    @SerializedName("image_url")
    val imageUrl: String?) : Parcelable