package com.workfort.pstuian.app.ui.donors.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.databinding.RowDonorBinding
import kotlin.random.Random

class DonorsViewHolder (val binding: RowDonorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(donor: DonorEntity) {
//        val randIndex = Random.nextInt(Const.backgroundList.size)
        with(binding) {
//            container.setBackgroundResource(Const.backgroundList[randIndex])
            tvName.text = donor.name
            tvInfo.text = donor.info
        }
    }
}