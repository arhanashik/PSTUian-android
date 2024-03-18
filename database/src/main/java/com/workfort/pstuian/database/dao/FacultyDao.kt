package com.workfort.pstuian.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames
import com.workfort.pstuian.model.FacultyEntity

@Dao
interface FacultyDao {
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