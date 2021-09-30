package com.workfort.pstuian.app.ui.home.faculty.holder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.databinding.RowDonorBinding

class DonorsViewHolder (val binding: RowDonorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(donor: DonorEntity) {
        binding.tvName.text = donor.name
        binding.tvInfo.text = donor.info
    }
}