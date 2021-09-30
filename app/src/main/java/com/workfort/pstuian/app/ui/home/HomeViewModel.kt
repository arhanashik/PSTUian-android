package com.workfort.pstuian.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.workfort.pstuian.app.data.local.database.DatabaseHelper
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyService
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.app.data.local.slider.SliderService
import kotlin.concurrent.thread

class HomeViewModel : ViewModel() {

    private var sliderService: SliderService = DatabaseHelper.provideSliderService()
    private lateinit var sliders: List<SliderEntity>

    private var facultyService: FacultyService = DatabaseHelper.provideFacultyService()
    private lateinit var facultiesLiveData: LiveData<List<FacultyEntity>>

    fun getSliders(): List<SliderEntity> {
        thread(start = true) {
            sliders = sliderService.getAll()
        }.join()

        return sliders
    }

    fun getFaculties(): LiveData<List<FacultyEntity>> {
        facultiesLiveData = facultyService.getAllLive()

        return facultiesLiveData
    }

    fun insertSliders(sliders: ArrayList<SliderEntity>) {
        this.sliders = sliders
        Thread { sliderService.insertAll(sliders)}.start()
    }

    fun insertFaculties(faculties: ArrayList<FacultyEntity>) {
        Thread { facultyService.insertAll(faculties) }.start()
    }

    fun clearAllData(): Boolean {
        thread(start = true) {
            Prefs.clear()
            DatabaseHelper.provideSliderService().deleteAll()
            DatabaseHelper.provideFacultyService().deleteAll()
            DatabaseHelper.provideTeacherService().deleteAll()
            DatabaseHelper.provideStudentService().deleteAll()
            DatabaseHelper.provideBatchService().deleteAll()
            DatabaseHelper.provideCourseScheduleService().deleteAll()
            DatabaseHelper.provideEmployeeService().deleteAll()
        }.join()

        return true
    }
}
