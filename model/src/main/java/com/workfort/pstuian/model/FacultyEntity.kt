package com.workfort.pstuian.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TableNames.FACULTY, indices = [Index(value = [ColumnNames.Faculty.SHORT_TITLE],
    unique = true)])
data class FacultyEntity (
    @PrimaryKey
    @ColumnInfo(name = ColumnNames.Faculty.ID)
    val id: Int,
    @ColumnInfo(name = ColumnNames.Faculty.SHORT_TITLE)
    @SerializedName("short_title")
    val shortTitle: String,
    @ColumnInfo(name = ColumnNames.Faculty.TITLE)
    val title: String,
    @ColumnInfo(name = ColumnNames.Faculty.ICON)
    val icon: String?,
) : Parcelable