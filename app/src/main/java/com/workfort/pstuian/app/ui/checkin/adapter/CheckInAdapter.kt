package com.workfort.pstuian.app.ui.checkin.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.checkin.CheckInEntity
import com.workfort.pstuian.app.ui.checkin.viewholder.CheckInViewHolder
import com.workfort.pstuian.app.ui.checkin.viewholder.DefaultCheckInViewHolder
import com.workfort.pstuian.databinding.RowCheckInUserBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 13:04.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/13.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class CheckInAdapter(
    private val onClickCheckIn : () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {

    /**
     * Setting extraViewCount to true will add a default view at the start of the list
     * */
    private val hasDefaultView = false
    object ViewType {
        const val Default = 0
        const val CheckIn = 1
    }

    val data : MutableList<CheckInEntity> = ArrayList()
    private val filteredData : MutableList<CheckInEntity> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<CheckInEntity>) {
        this.data.clear()
        this.data.addAll(data)
        filterData(data)
    }

    fun addData(data: MutableList<CheckInEntity>) {
        if(data.isEmpty()) return

        val newData = if(this.data.isEmpty()) data else data.toSet().minus(this.data.toSet())
        if(newData.isEmpty()) return

        val startPosition = this.data.size
        this.data.addAll(newData)
        this.filteredData.addAll(newData)

        val newDataCount = newData.size
        if(newDataCount == 1) notifyItemInserted(startPosition)
        else notifyItemRangeInserted(startPosition, newDataCount)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: MutableList<CheckInEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= filteredData.size && filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        filteredData.clear()
        filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun clear() {
        val startPosition = if(hasDefaultView) 1 else 0
        val removedItemCount = filteredData.size
        this.data.clear()
        this.filteredData.clear()
        notifyItemRangeRemoved(startPosition, removedItemCount)
    }

    override fun getItemCount(): Int = filteredData.size.plus(if(hasDefaultView) 1 else 0)

    override fun getItemViewType(position: Int): Int {
        return if(hasDefaultView && position == 0) ViewType.Default else ViewType.CheckIn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            ViewType.Default -> DefaultCheckInViewHolder(
                RowCheckInUserBinding.inflate(inflater, parent, false)
            )
            else -> CheckInViewHolder(
                RowCheckInUserBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DefaultCheckInViewHolder -> {
                holder.bind(onClickCheckIn)
            }
            is CheckInViewHolder -> {
                val realPosition = position.minus(if(hasDefaultView) 1 else 0)
                holder.itemView.animation = AnimationUtils.loadAnimation(
                    holder.itemView.context, R.anim.anim_item_insert)
                holder.bind(filteredData[realPosition])
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<CheckInEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(data)
                } else {
                    val q = query.toString().lowercase(Locale.ROOT)
                    data.forEach {
                        if(it.name.lowercase(Locale.ROOT).contains(q)
                            || (it.batch).lowercase(Locale.ROOT).contains(q))
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
                val newData = filteredResult?.values as ArrayList<CheckInEntity>
                filterData(newData)
            }
        }
    }
}