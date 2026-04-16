package com.workfort.pstuian.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.featuredomain.model.BatchEntity

@Entity(tableName = "batch")
data class BatchDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "session")
    val session: String,
    @ColumnInfo(name = "faculty_id")
    val facultyId: Int,
    @ColumnInfo(name = "total_student")
    val totalStudent: Int,
    @ColumnInfo(name = "registered_student")
    val registeredStudent: Int,
)

fun BatchDbEntity.toDomain() = BatchEntity(
    id = id,
    name = name,
    title = title,
    session = session,
    facultyId = facultyId,
    totalStudent = totalStudent,
    registeredStudent = registeredStudent,
)

fun BatchEntity.toDb() = BatchDbEntity(
    id = id,
    name = name,
    title = title,
    session = session,
    facultyId = facultyId,
    totalStudent = totalStudent,
    registeredStudent = registeredStudent,
)
