package com.workfort.pstuian.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames

@Entity(tableName = TableNames.SLIDER)
data class SliderEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Slider.TITLE) val title: String?,
    @ColumnInfo(name = ColumnNames.Slider.IMAGE_URL)
    val imageUrl: String?,
)
