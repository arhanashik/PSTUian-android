package com.workfort.pstuian.app.data.local.faculty

import androidx.lifecycle.LiveData
import androidx.room.*
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Dao
interface FacultyDao {
    @Query("SELECT * FROM " + TableNames.FACULTY
            + " ORDER BY " + ColumnNames.Faculty.TITLE + " ASC")
    fun getAllLive(): LiveData<List<FacultyEntity>>

    @Query("SELECT * FROM " + TableNames.FACULTY
            + " ORDER BY " + ColumnNames.Faculty.TITLE + " ASC")
    suspend fun getAll(): List<FacultyEntity>

    @Query("SELECT * FROM " + TableNames.FACULTY
            + " WHERE " + ColumnNames.Faculty.ID + " = :id")
    suspend fun get(id: Int): FacultyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(facultyEntity: FacultyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(facultyEntities: List<FacultyEntity>)

    @Update
    fun update(facultyEntity: FacultyEntity)

    @Delete
    fun delete(facultyEntity: FacultyEntity)

    @Query("DELETE FROM " + TableNames.FACULTY)
    suspend fun deleteAll()
}