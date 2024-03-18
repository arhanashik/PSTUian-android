package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.model.DonorEntity

interface DonorClickEvent{
    fun onClickDonor(donor: DonorEntity)
}