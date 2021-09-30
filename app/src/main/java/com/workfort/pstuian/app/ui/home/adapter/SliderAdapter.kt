package com.workfort.pstuian.app.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.smarteist.autoimageslider.SliderViewAdapter
import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.databinding.ItemSliderBinding


/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 3:09 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class SliderAdapter : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    private var sliders: MutableList<SliderEntity> = ArrayList()

    fun setSliders(sliderItems: MutableList<SliderEntity>) {
        sliders.clear()
        sliders.addAll(sliderItems)
        notifyDataSetChanged()
    }

    fun deleteSlider(position: Int) {
        sliders.removeAt(position)
        notifyDataSetChanged()
    }

    fun addSlider(slider: SliderEntity) {
        sliders.add(slider)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSliderBinding.inflate(inflater, null, false)
        return SliderAdapterVH(binding)
    }
    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val slider = sliders[position]
        viewHolder.bind(slider)
    }

    override fun getCount(): Int {
        return sliders.size
    }

    inner class SliderAdapterVH(private val binding: ItemSliderBinding) : ViewHolder(binding.root) {
        fun bind(slider: SliderEntity) {
            with(binding) {
                tvAutoImageSlider.text = slider.title
                ivAutoImageSlider.load(slider.imageUrl)
            }
        }
    }
}