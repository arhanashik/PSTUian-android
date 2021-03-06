package com.workfort.pstuian.app.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.app.ui.base.callback.ItemClickEvent
import com.workfort.pstuian.databinding.ItemSliderBinding
import com.workfort.pstuian.util.view.imageslider.SliderViewAdapter


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

class SliderAdapter(
    private val listener: ItemClickEvent<SliderEntity>? = null
) : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    private var sliders: MutableList<SliderEntity> = ArrayList()

    fun setItems(sliderItems: MutableList<SliderEntity>) {
        sliders.clear()
        sliders.addAll(sliderItems)
        notifyDataSetChanged()
    }

    fun delete(position: Int) {
        sliders.removeAt(position)
        notifyDataSetChanged()
    }

    fun add(slider: SliderEntity) {
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
                lavImagePlaceholder.visibility =View.VISIBLE
                lavError.visibility =View.GONE
                ivAutoImageSlider.load(slider.imageUrl) {
                    listener(onError = { _, _->
                            lavImagePlaceholder.visibility =View.GONE
                            lavError.visibility =View.VISIBLE
                        }, onSuccess = { _, _->
                            lavImagePlaceholder.visibility =View.GONE
                        })
                }
                ivAutoImageSlider.setOnClickListener {
                    listener?.onClickItem(slider)
                }
            }
        }
    }
}