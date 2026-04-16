package com.workfort.pstuian.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.workfort.pstuian.featuredomain.model.FacultyEntity

@Entity(tableName = "faculty", indices = [Index(value = ["short_title"], unique = true)])
data class FacultyDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "short_title")
    val shortTitle: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "icon")
    val icon: String?,
)

fun FacultyDbEntity.toDomain() = FacultyEntity(
    id = id,
    shortTitle = shortTitle,
    title = title,
    icon = icon,
)

fun FacultyEntity.toDb() = FacultyDbEntity(
    id = id,
    shortTitle = shortTitle,
    title = title,
    icon = icon,
)
