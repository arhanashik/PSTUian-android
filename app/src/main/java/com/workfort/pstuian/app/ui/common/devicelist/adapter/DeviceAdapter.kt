package com.workfort.pstuian.app.ui.common.devicelist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.app.ui.common.devicelist.viewholder.DeviceViewHolder
import com.workfort.pstuian.databinding.RowDeviceBinding

/**
 *  ****************************************************************************
 *  * Created by : arhan on 23 Dec, 2021 at 22:36.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class DeviceAdapter (
    private val onClick : (item: DeviceEntity) -> Unit
) : RecyclerView.Adapter<DeviceViewHolder>() {
    private val _data : MutableList<DeviceEntity> = ArrayList()
    val data: List<DeviceEntity> get() = _data
    private val _filteredData : MutableList<DeviceEntity> = ArrayList()

    fun setData(data: List<DeviceEntity>) {
        _data.clear()
        _data.addAll(data)
        filterData(data)
    }

    fun addData(data: List<DeviceEntity>) {
        if(data.isEmpty()) return

        val newData = if(this._data.isEmpty()) data else data.toSet().minus(this._data.toSet())
        if(newData.isEmpty()) return

        val startPosition = this._data.size
        this._data.addAll(newData)
        this._filteredData.addAll(newData)

        val newDataCount = newData.size
        if(newDataCount == 1) notifyItemInserted(startPosition)
        else notifyItemRangeInserted(startPosition, newDataCount)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: List<DeviceEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= _filteredData.size && _filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        _filteredData.clear()
        _filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun update(item: DeviceEntity) {
        // find the old item or return
        val oldItem = _data.firstOrNull { it.id == item.id }?: return

        // replace from the main list
        val position = _data.indexOf(oldItem)
        _data[position] = item

        // replace from the visible list
        val viewPosition = _filteredData.indexOf(oldItem)
        if(viewPosition >= 0) {
            _filteredData[viewPosition] = item
            notifyItemChanged(viewPosition)
        }
    }

    fun remove(itemId: String) {
        // find the old item or return
        val oldItem = _data.firstOrNull { it.id == itemId }?: return

        // remove from the main list
        if(!_data.remove(oldItem)) return

        // remove from the visible list
        val viewPosition = _filteredData.indexOf(oldItem)
        if(viewPosition >= 0) {
            _filteredData.removeAt(viewPosition)
            notifyItemRemoved(viewPosition)
        }
    }

    fun clear() {
        val removedItemCount = _filteredData.size
        _data.clear()
        _filteredData.clear()
        notifyItemRangeRemoved(0, removedItemCount)
    }

    override fun getItemCount(): Int = _filteredData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DeviceViewHolder(
            RowDeviceBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(_filteredData[position], onClick)
    }
}