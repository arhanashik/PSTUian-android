package com.workfort.pstuian.app.data.local.batch

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TableNames.BATCH)
data class BatchEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Batch.NAME) val name: String,
    @ColumnInfo(name = ColumnNames.Batch.TITLE) val title: String?,
    @ColumnInfo(name = ColumnNames.Batch.SESSION) val session: String,
    @ColumnInfo(name = ColumnNames.Batch.FACULTY_ID)
    @SerializedName("faculty_id") val facultyId: Int,
    @ColumnInfo(name = ColumnNames.Batch.TOTAL_STUDENT)
    @SerializedName("total_student") val totalStudent: Int,
    @ColumnInfo(name = ColumnNames.Batch.REGISTERED_STUDENT)
    @SerializedName("registered_student") val registeredStudent: Int
) : Parcelable