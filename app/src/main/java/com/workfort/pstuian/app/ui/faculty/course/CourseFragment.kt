package com.workfort.pstuian.app.ui.faculty.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.base.fragment.BaseFragment
import com.workfort.pstuian.app.ui.faculty.adapter.CourseAdapter
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.listener.CourseScheduleClickEvent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.faculty.viewstate.CourseState
import com.workfort.pstuian.databinding.FragmentCourseScheduleBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CourseFragment(private val faculty: FacultyEntity)
    : BaseFragment<FragmentCourseScheduleBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCourseScheduleBinding
            = FragmentCourseScheduleBinding::inflate

    companion object {
        fun newInstance(faculty: FacultyEntity) = CourseFragment(faculty)
    }

    private val mViewModel: FacultyViewModel by viewModel()
    private lateinit var mAdapter: CourseAdapter

    override fun afterOnViewCreated(view: View, savedInstanceState: Bundle?) {
        initCourseList()
        observeCourses()
        mViewModel.facultyId = faculty.id
        loadData()
        with(binding) {
            srlReloadData.setOnRefreshListener { loadData(true) }
            btnRefresh.setOnClickListener { loadData(true) }
        }
    }

    private fun initCourseList() {
        mAdapter = CourseAdapter()
        mAdapter.setListener(object: CourseScheduleClickEvent {
            override fun onClickCourseSchedule(course: CourseEntity) {

            }
        })
        binding.rvData.adapter = mAdapter
    }

    private fun loadData(forceRefresh: Boolean = false) {
        mViewModel.courseStateForceRefresh = forceRefresh
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetCourses)
        }
    }

    private fun observeCourses() {
        lifecycleScope.launch {
            mViewModel.courseState.collect {
                when (it) {
                    is CourseState.Idle -> Unit
                    is CourseState.Loading -> setActionUiState(true)
                    is CourseState.Courses -> {
                        setActionUiState(false)
                        renderCourses(it.course)
                    }
                    is CourseState.Error -> {
                        setActionUiState(false)
                        renderCourses(emptyList())
                        binding.tvMessage.text = it.error?: "Can't load data"
                    }
                }
            }
        }
    }

    private fun setActionUiState(isLoading: Boolean) {
        val inverseVisibility = if(isLoading) View.GONE else View.VISIBLE
        with(binding) {
            srlReloadData.isRefreshing = isLoading
            rvData.visibility = inverseVisibility
            lavError.visibility = inverseVisibility
            tvMessage.visibility = inverseVisibility
            btnRefresh.visibility = inverseVisibility
        }
    }

    private fun renderCourses(data: List<CourseEntity>) {
        val visibility = if(data.isEmpty()) View.GONE else View.VISIBLE
        val inverseVisibility = if(data.isEmpty()) View.VISIBLE else View.GONE
        with(binding) {
            rvData.visibility = visibility
            srlReloadData.visibility = visibility
            lavError.visibility = inverseVisibility
            if(data.isEmpty()) lavError.playAnimation()
            tvMessage.visibility = inverseVisibility
            btnRefresh.visibility = inverseVisibility
        }
        mAdapter.setData(data.toMutableList())
    }

    fun filter(query: String) {
        mAdapter.filter.filter(query)
    }
}
