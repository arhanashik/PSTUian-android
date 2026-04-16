package com.workfort.pstuian.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.data.local.database.entity.StudentDbEntity

@Dao
interface StudentDao {
    @Query("SELECT * FROM student ORDER BY id ASC")
    suspend fun getAll(): List<StudentDbEntity>

    @Query("SELECT * FROM student WHERE faculty_id=:facultyId AND batch_id=:batchId ORDER BY id ASC")
    suspend fun getAll(facultyId: Int, batchId: Int): List<StudentDbEntity>

    @Query("SELECT * FROM student WHERE id = :id")
    suspend fun get(id: Int): StudentDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(studentEntity: StudentDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<StudentDbEntity>)

    @Update
    suspend fun update(studentEntity: StudentDbEntity)

    @Delete
    suspend fun delete(studentEntity: StudentDbEntity)

    @Query("DELETE FROM student WHERE faculty_id=:faculty")
    suspend fun deleteAll(faculty: Int)

    @Query("DELETE FROM student WHERE faculty_id=:faculty AND batch_id=:batch")
    suspend fun deleteAll(faculty: Int, batch: Int)

    @Query("DELETE FROM student")
    suspend fun deleteAll()
}