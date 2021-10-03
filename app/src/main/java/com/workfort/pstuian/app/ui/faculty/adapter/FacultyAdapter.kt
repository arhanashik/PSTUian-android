package com.workfort.pstuian.app.ui.faculty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.faculty.viewholder.FacultyViewHolder
import com.workfort.pstuian.app.ui.faculty.listener.FacultyClickEvent
import com.workfort.pstuian.databinding.RowFacultyBinding
import com.workfort.pstuian.util.helper.MathHelper

class FacultyAdapter : RecyclerView.Adapter<FacultyViewHolder>() {
    private val faculties : MutableList<FacultyEntity> = ArrayList()
    private var listener: FacultyClickEvent? = null

    private val backgroundIndexArr = arrayListOf(0,1,2,3,4,5,6,7,8)

    fun setFaculties(faculties: MutableList<FacultyEntity>) {
        this.faculties.clear()
        this.faculties.addAll(faculties)
        notifyDataSetChanged()

        MathHelper.shuffle(backgroundIndexArr)
    }

    fun setListener(listener: FacultyClickEvent) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return faculties.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowFacultyBinding.inflate(inflater, parent, false)
        return FacultyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FacultyViewHolder, position: Int) {
        val faculty = faculties[position]
        val background = Const.backgroundList[backgroundIndexArr[position]]

        holder.bind(faculty, background)
        holder.binding.root.setOnClickListener {
            listener?.onClickFaculty(faculty)
        }
    }
}