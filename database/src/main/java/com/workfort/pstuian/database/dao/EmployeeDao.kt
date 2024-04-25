package com.workfort.pstuian.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames
import com.workfort.pstuian.model.EmployeeEntity

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM " + TableNames.EMPLOYEE
            + " ORDER BY " + ColumnNames.Employee.NAME + " ASC")
    fun getAll(): List<EmployeeEntity>

    @Query("SELECT * FROM " + TableNames.EMPLOYEE
            + " WHERE " + ColumnNames.Employee.FACULTY_ID + "=:facultyId"
            + " ORDER BY " + ColumnNames.Employee.NAME + " ASC")
    suspend fun getAll(facultyId: Int): List<EmployeeEntity>

    @Query("SELECT * FROM " + TableNames.EMPLOYEE
            + " WHERE " + ColumnNames.Employee.ID + "=:id LIMIT 1")
    fun get(id: Int): EmployeeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employeeEntity: EmployeeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<EmployeeEntity>)

    @Update
    fun update(employeeEntity: EmployeeEntity)

    @Delete
    fun delete(employeeEntity: EmployeeEntity)

    @Query("DELETE FROM " + TableNames.EMPLOYEE
            + " WHERE " + ColumnNames.Employee.FACULTY_ID + "=:faculty")
    suspend fun deleteAll(faculty: String)

    @Query("DELETE FROM " + TableNames.EMPLOYEE)
    suspend fun deleteAll()
}