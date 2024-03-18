package com.workfort.pstuian.app.ui.common.blooddonationlist.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.BloodDonationEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.blooddonationlist.viewholder.BloodDonationViewHolder
import com.workfort.pstuian.databinding.RowBloodDonationBinding
import java.util.Locale

/**
 *  ****************************************************************************
 *  * Created by : arhan on 16 Dec, 2021 at 3:26.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class BloodDonationListAdapter (
    private val showAction: Boolean,
    private val onClickEdit : (item: BloodDonationEntity) -> Unit,
    private val onClickDelete : (item: BloodDonationEntity) -> Unit,
) : RecyclerView.Adapter<BloodDonationViewHolder>(),
    Filterable {

    private val _data : MutableList<BloodDonationEntity> = ArrayList()
    val data: List<BloodDonationEntity> get() = _data
    private val _filteredData : MutableList<BloodDonationEntity> = ArrayList()

    fun setData(data: List<BloodDonationEntity>) {
        _data.clear()
        _data.addAll(data)
        filterData(data)
    }

    fun addData(data: List<BloodDonationEntity>) {
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
    fun filterData(data: List<BloodDonationEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= _filteredData.size && _filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        _filteredData.clear()
        _filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun add(item: BloodDonationEntity, first: Boolean = false) {
        // check if item already exists
        val oldItem = _data.firstOrNull { it.id == item.id }
        if(oldItem != null) return

        // add in the list
        if(first) {
            _data.add(0, item)
            _filteredData.add(0, item)
            notifyItemInserted(0)
        } else {
            _data.add(item)
            _filteredData.add(item)
            notifyItemInserted(_filteredData.size - 1)
        }
    }

    fun update(item: BloodDonationEntity) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodDonationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BloodDonationViewHolder(
            RowBloodDonationBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BloodDonationViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(showAction, _filteredData[position], onClickEdit, onClickDelete)
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<BloodDonationEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(_data)
                } else {
                    val q = query.toString().lowercase(Locale.ROOT)
                    _data.forEach {
                        val requestId = if(it.requestId == null) "Unregistered" else
                            it.requestId.toString()
                        if(it.date.lowercase(Locale.ROOT).contains(q)
                            || requestId.lowercase(Locale.ROOT).contains(q))
                            result.add(it)
                    }
                }

                val filteredResult = FilterResults()
                filteredResult.count = result.size
                filteredResult.values = result

                return filteredResult
            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                val newData = filteredResult?.values as ArrayList<BloodDonationEntity>
                filterData(newData)
            }
        }
    }
}