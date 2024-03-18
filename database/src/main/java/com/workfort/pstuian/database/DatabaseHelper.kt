package com.workfort.pstuian.database

interface DatabaseHelper {
    fun provideSliderService()
    fun provideFacultyService()
    fun provideStudentService()
    fun provideTeacherService()
    fun provideBatchService()
    fun provideEmployeeService()
    fun provideCourseScheduleService()
}