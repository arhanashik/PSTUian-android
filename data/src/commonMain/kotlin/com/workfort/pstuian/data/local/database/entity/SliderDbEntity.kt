package com.workfort.pstuian.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.featuredomain.model.SliderEntity

@Entity(tableName = "slider")
data class SliderDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
)

fun SliderDbEntity.toDomain() = SliderEntity(
    id = id,
    title = title,
    imageUrl = imageUrl,
)

fun SliderEntity.toDb() = SliderDbEntity(
    id = id,
    title = title,
    imageUrl = imageUrl,
)
