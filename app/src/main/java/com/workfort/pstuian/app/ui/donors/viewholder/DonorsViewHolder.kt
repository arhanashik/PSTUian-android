package com.workfort.pstuian.app.ui.donors.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.DonorEntity
import com.workfort.pstuian.databinding.RowDonorBinding

class DonorsViewHolder (val binding: RowDonorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(donor: DonorEntity) {
        with(binding) {
            tvName.text = donor.name
            tvInfo.text = donor.info
        }
    }
}