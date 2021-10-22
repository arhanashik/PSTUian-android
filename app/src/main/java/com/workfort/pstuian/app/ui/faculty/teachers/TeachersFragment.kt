package com.workfort.pstuian.app.ui.faculty.teachers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.ui.base.fragment.BaseFragment
import com.workfort.pstuian.app.ui.faculty.adapter.TeachersAdapter
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.listener.TeacherClickEvent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.faculty.viewstate.TeacherState
import com.workfort.pstuian.app.ui.teacherprofile.TeacherProfileActivity
import com.workfort.pstuian.databinding.FragmentTeachersBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TeachersFragment(private val faculty: FacultyEntity)
    : BaseFragment<FragmentTeachersBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTeachersBinding
            = FragmentTeachersBinding::inflate

    companion object {
        fun newInstance(faculty: FacultyEntity) = TeachersFragment(faculty)
    }

    private val mViewModel: FacultyViewModel by viewModel()
    private lateinit var mAdapter: TeachersAdapter

    override fun afterOnViewCreated(view: View, savedInstanceState: Bundle?) {
        initTeacherList()

        observeTeachers()
        mViewModel.facultyId = faculty.id
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetTeachers)
        }
    }

    private fun initTeacherList() {
        mAdapter = TeachersAdapter()
        mAdapter.setListener(object: TeacherClickEvent {
            override fun onClickTeacher(teacher: TeacherEntity) {
                val intent = Intent(context, TeacherProfileActivity::class.java)
                intent.putExtra(Const.Key.FACULTY, faculty)
                intent.putExtra(Const.Key.TEACHER, teacher)
                startActivity(intent)
            }
        })
        binding.rvTeachers.layoutManager = LinearLayoutManager(context)
        binding.rvTeachers.adapter = mAdapter
    }

    private fun observeTeachers() {
        lifecycleScope.launch {
            mViewModel.teacherState.collect {
                when (it) {
                    is TeacherState.Idle -> {
                    }
                    is TeacherState.Loading -> {
                        binding.tvMessage.visibility = View.INVISIBLE
                        binding.loader.visibility = View.VISIBLE
                        binding.rvTeachers.visibility = View.INVISIBLE
                    }
                    is TeacherState.Teachers -> {
                        binding.tvMessage.visibility = View.INVISIBLE
                        binding.loader.visibility = View.GONE
                        binding.rvTeachers.visibility = View.VISIBLE
                        renderTeachers(it.teachers)
                    }
                    is TeacherState.Error -> {
                        binding.tvMessage.visibility = View.VISIBLE
                        binding.loader.visibility = View.GONE
                        binding.rvTeachers.visibility = View.INVISIBLE
                        Timber.e(it.error?: "Can't load data")
                    }
                }
            }
        }
    }

    private fun renderTeachers(teachers: List<TeacherEntity>) {
        mAdapter.setTeachers(teachers.toMutableList())
    }

    fun filter(query: String) {
        mAdapter.filter.filter(query)
    }
}
