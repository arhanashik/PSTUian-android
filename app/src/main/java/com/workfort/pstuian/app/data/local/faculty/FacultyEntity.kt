package com.workfort.pstuian.app.data.local.faculty

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
@Entity(tableName = TableNames.FACULTY, indices = [Index(value = [ColumnNames.Faculty.SHORT_TITLE],
    unique = true)])
data class FacultyEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Faculty.SHORT_TITLE)
    @SerializedName("short_title")
    val shortTitle: String,
    @ColumnInfo(name = ColumnNames.Faculty.TITLE) val title: String) : Parcelable