package com.workfort.pstuian.ui.donors.state

import com.workfort.pstuian.featuredomain.model.DonorEntity

sealed interface DonorsMessageState {
    data class ShowDonorDetails(val donor: DonorEntity) : DonorsMessageState
    data class Error(val message: String) : DonorsMessageState
}
