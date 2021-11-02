package com.workfort.pstuian.app.data.local.employee

import androidx.lifecycle.LiveData
import androidx.room.*
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM " + TableNames.EMPLOYEE
            + " ORDER BY " + ColumnNames.Employee.NAME + " ASC")
    fun getAllLive(): LiveData<List<EmployeeEntity>>

    @Query("SELECT * FROM " + TableNames.EMPLOYEE
            + " WHERE " + ColumnNames.Employee.FACULTY_ID + "=:facultyId"
            + " ORDER BY " + ColumnNames.Employee.NAME + " ASC")
    fun getAllLive(facultyId: Int): LiveData<List<EmployeeEntity>>

    @Query("SELECT * FROM " + TableNames.EMPLOYEE
            + " ORDER BY " + ColumnNames.Employee.NAME + " ASC")
    fun getAll(): List<EmployeeEntity>

    @Query("SELECT * FROM " + TableNames.EMPLOYEE
            + " WHERE " + ColumnNames.Employee.FACULTY_ID + "=:facultyId"
            + " ORDER BY " + ColumnNames.Employee.NAME + " ASC")
    suspend fun getAll(facultyId: Int): List<EmployeeEntity>

    @Query("SELECT * FROM " + TableNames.EMPLOYEE
            + " WHERE " + ColumnNames.Employee.ID + "=:id LIMIT 1")
    fun get(id: String): EmployeeEntity

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