package com.workfort.pstuian.app.data.local.slider

import androidx.lifecycle.LiveData

class SliderService(private val sliderDao: SliderDao) {
    fun getAllLive(): LiveData<List<SliderEntity>> {
        return sliderDao.getAllLive()
    }

    fun getAll(): List<SliderEntity> {
        return sliderDao.getAll()
    }

    fun get(id: String) : SliderEntity{
        return sliderDao.get(id)
    }

    fun insert(slider: SliderEntity) {
        sliderDao.insert(slider)
    }

    fun insertAll(sliders: List<SliderEntity>) {
        sliderDao.insertAll(sliders)
    }

    fun update(slider: SliderEntity) {
        sliderDao.update(slider)
    }

    fun delete(slider: SliderEntity) {
        sliderDao.delete(slider)
    }

    fun deleteAll() {
        sliderDao.deleteAll()
    }
}