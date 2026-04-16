package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.SliderDao
import com.workfort.pstuian.data.local.database.entity.toDb
import com.workfort.pstuian.data.local.database.entity.toDomain
import com.workfort.pstuian.featuredomain.model.SliderEntity

class SliderDbService(private val sliderDao: SliderDao) {
    suspend fun getAll(): List<SliderEntity> = sliderDao.getAll().map { it.toDomain() }
    suspend fun get(id: Int) : SliderEntity? = sliderDao.get(id)?.toDomain()
    suspend fun insert(slider: SliderEntity) = sliderDao.insert(slider.toDb())
    suspend fun insertAll(sliders: List<SliderEntity>) = sliderDao.insertAll(sliders.map { it.toDb() })
    suspend fun update(slider: SliderEntity) = sliderDao.update(slider.toDb())
    suspend fun delete(slider: SliderEntity) = sliderDao.delete(slider.toDb())
    suspend fun deleteAll() = sliderDao.deleteAll()
}
