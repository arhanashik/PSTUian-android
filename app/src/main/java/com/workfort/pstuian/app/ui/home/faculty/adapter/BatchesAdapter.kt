package com.workfort.pstuian.app.ui.home.faculty.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.databinding.RowBatchBinding
import com.workfort.pstuian.app.ui.home.faculty.holder.BatchesViewHolder
import com.workfort.pstuian.app.ui.home.faculty.listener.BatchClickEvent

class BatchesAdapter : RecyclerView.Adapter<BatchesViewHolder>(), Filterable {

    private val batches : MutableList<BatchEntity> = ArrayList()
    private val filteredBatches : MutableList<BatchEntity> = ArrayList()
    private var listener: BatchClickEvent? = null

    fun setBatches(batches: MutableList<BatchEntity>) {
        this.batches.clear()
        this.batches.addAll(batches)

        filter.filter("")
    }

    fun setListener(listener: BatchClickEvent) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<BatchEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(batches)
                }else {
                    val q = query.toString().toLowerCase()
                    batches.forEach {
                        if(it.name!!.toLowerCase().contains(q)
                            || it.title!!.toLowerCase().contains(q)
                            || it.session!!.toLowerCase().contains(q))
                            result.add(it)
                    }
                }

                val filteredResult = FilterResults()
                filteredResult.count = result.size
                filteredResult.values = result

                return filteredResult
            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                filteredBatches.clear()
                filteredBatches.addAll(filteredResult?.values as ArrayList<BatchEntity>)

                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredBatches.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.row_batch, parent, false)
                as RowBatchBinding
        return BatchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchesViewHolder, position: Int) {
        val batch = filteredBatches[position]

        holder.bind(batch)
        holder.binding.root.setOnClickListener { view ->
            run {
                if(listener != null) listener?.onClickBatch(batch)
            }
        }
        holder.binding.tvViewList.setOnClickListener { view ->
            run {
                if(listener != null) listener?.onClickBatch(batch)
            }
        }
    }
}