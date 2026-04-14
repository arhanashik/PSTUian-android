package com.workfort.pstuian.featuredomain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames

@Entity(tableName = "faculty", indices = [Index(value = ["shortTitle"], unique = true)])
data class FacultyEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "shortTitle")
    val shortTitle: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "icon")
    val icon: String?,
)
