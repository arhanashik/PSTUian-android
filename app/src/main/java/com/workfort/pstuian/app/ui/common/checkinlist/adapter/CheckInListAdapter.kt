package com.workfort.pstuian.app.ui.common.checkinlist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.app.ui.common.checkinlist.viewholder.MyCheckInViewHolder
import com.workfort.pstuian.databinding.RowMyCheckInBinding

/**
 *  ****************************************************************************
 *  * Created by : arhan on 15 Dec, 2021 at 22:43.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class CheckInListAdapter(
    private val onClick : (item: CheckInEntity) -> Unit
) : RecyclerView.Adapter<MyCheckInViewHolder>() {
    private val _data : MutableList<CheckInEntity> = ArrayList()
    val data: List<CheckInEntity> get() = _data
    private val _filteredData : MutableList<CheckInEntity> = ArrayList()

    fun setData(data: List<CheckInEntity>) {
        _data.clear()
        _data.addAll(data)
        filterData(data)
    }

    fun addData(data: List<CheckInEntity>) {
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
    fun filterData(data: List<CheckInEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= _filteredData.size && _filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        _filteredData.clear()
        _filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun update(item: CheckInEntity) {
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

    fun remove(itemId: Int) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCheckInViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyCheckInViewHolder(
            RowMyCheckInBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyCheckInViewHolder, position: Int) {
        holder.bind(_filteredData[position], onClick)
    }
}