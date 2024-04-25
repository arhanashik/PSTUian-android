package com.workfort.pstuian.database.service

import com.workfort.pstuian.database.dao.SliderDao
import com.workfort.pstuian.model.SliderEntity

class SliderDbService(private val sliderDao: SliderDao) {
    suspend fun getAll(): List<SliderEntity> = sliderDao.getAll()
    fun get(id: String) : SliderEntity = sliderDao.get(id)
    fun insert(slider: SliderEntity) = sliderDao.insert(slider)
    suspend fun insertAll(sliders: List<SliderEntity>) = sliderDao.insertAll(sliders)
    fun update(slider: SliderEntity) = sliderDao.update(slider)
    fun delete(slider: SliderEntity) = sliderDao.delete(slider)
    suspend fun deleteAll() = sliderDao.deleteAll()
}