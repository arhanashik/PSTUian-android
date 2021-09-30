package com.workfort.pstuian.app.data.remote

import com.workfort.pstuian.app.data.local.slider.SliderEntity

data class SliderResponse(val success: Boolean,
                          val message: String,
                          val sliders: ArrayList<SliderEntity>)
