package com.workfort.pstuian.featuredomain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import com.workfort.pstuian.featuredomain.appconstant.ColumnNames
import com.workfort.pstuian.featuredomain.appconstant.TableNames

@Serializable
@Entity(tableName = TableNames.BATCH)
data class BatchEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Batch.NAME) val name: String,
    @ColumnInfo(name = ColumnNames.Batch.TITLE) val title: String?,
    @ColumnInfo(name = ColumnNames.Batch.SESSION) val session: String,
    @ColumnInfo(name = ColumnNames.Batch.FACULTY_ID) val facultyId: Int,
    @ColumnInfo(name = ColumnNames.Batch.TOTAL_STUDENT) val totalStudent: Int,
    @ColumnInfo(name = ColumnNames.Batch.REGISTERED_STUDENT) val registeredStudent: Int,
)
