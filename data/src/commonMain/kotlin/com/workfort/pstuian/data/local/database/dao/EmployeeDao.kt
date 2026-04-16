package com.workfort.pstuian.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.data.local.database.entity.EmployeeDbEntity

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employee ORDER BY name ASC")
    suspend fun getAll(): List<EmployeeDbEntity>

    @Query("SELECT * FROM employee WHERE faculty_id=:facultyId ORDER BY name ASC")
    suspend fun getAll(facultyId: Int): List<EmployeeDbEntity>

    @Query("SELECT * FROM employee WHERE id=:id LIMIT 1")
    suspend fun get(id: Int): EmployeeDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employeeEntity: EmployeeDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<EmployeeDbEntity>)

    @Update
    suspend fun update(employeeEntity: EmployeeDbEntity)

    @Delete
    suspend fun delete(employeeEntity: EmployeeDbEntity)

    @Query("DELETE FROM employee WHERE faculty_id=:faculty")
    suspend fun deleteAll(faculty: Int)

    @Query("DELETE FROM employee")
    suspend fun deleteAll()
}