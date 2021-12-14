package com.workfort.pstuian.app.ui.common.locationpicker.adpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.checkinlocation.CheckInLocationEntity
import com.workfort.pstuian.app.ui.common.locationpicker.viewholder.CheckInLocationViewHolder
import com.workfort.pstuian.app.ui.common.locationpicker.viewholder.CreateCheckInLocationViewHolder
import com.workfort.pstuian.databinding.RowCreateLocationBinding
import com.workfort.pstuian.databinding.RowLocationBinding

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 22:52.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/14.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class CheckInLocationAdapter(
    private val onCreateNew : () -> Unit,
    private val onSelect : (location: CheckInLocationEntity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * Setting extraViewCount to true will add a default view at the start of the list
     * */
    private var hasDefaultView = false
    object ViewType {
        const val Default = 0
        const val CheckIn = 1
    }

    private val _data : MutableList<CheckInLocationEntity> = ArrayList()
    val data: List<CheckInLocationEntity> get() = _data
    private val filteredData : MutableList<CheckInLocationEntity> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<CheckInLocationEntity>) {
        _data.clear()
        _data.addAll(data)
        filterData(data)
    }

    fun addData(data: List<CheckInLocationEntity>) {
        if(data.isEmpty()) return

        val newData = if(this._data.isEmpty()) data else data.toSet().minus(this._data.toSet())
        if(newData.isEmpty()) return

        val startPosition = this._data.size
        this._data.addAll(newData)
        this.filteredData.addAll(newData)

        val newDataCount = newData.size
        if(newDataCount == 1) notifyItemInserted(startPosition)
        else notifyItemRangeInserted(startPosition, newDataCount)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: List<CheckInLocationEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= filteredData.size && filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        filteredData.clear()
        filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun showDefaultView(show: Boolean) {
        if(hasDefaultView == show) return

        hasDefaultView = show
        if(show) notifyItemInserted(0)
        else notifyItemRemoved(0)
    }

    fun clear() {
        val startPosition = if(hasDefaultView) 1 else 0
        val removedItemCount = filteredData.size
        _data.clear()
        filteredData.clear()
        notifyItemRangeRemoved(startPosition, removedItemCount)
    }

    override fun getItemCount(): Int = filteredData.size.plus(if(hasDefaultView) 1 else 0)

    override fun getItemViewType(position: Int): Int {
        return if(hasDefaultView && position == 0) ViewType.Default else ViewType.CheckIn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            ViewType.Default -> CreateCheckInLocationViewHolder(
                RowCreateLocationBinding.inflate(inflater, parent, false)
            )
            else -> CheckInLocationViewHolder(
                RowLocationBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CreateCheckInLocationViewHolder -> holder.bind(onCreateNew)
            is CheckInLocationViewHolder -> {
                val realPosition = position.minus(if(hasDefaultView) 1 else 0)
                holder.bind(filteredData[realPosition], onSelect)
            }
        }
    }
}