package com.workfort.pstuian.app.ui.home.faculty.listener

import com.workfort.pstuian.app.data.local.donor.DonorEntity

interface DonorClickEvent{
    fun onClickDonor(donor: DonorEntity)
}