package com.workfort.pstuian.app.ui.faculty.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
import timber.log.Timber

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
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetCourses)
        }
    }

    private fun initCourseList() {
        mAdapter = CourseAdapter()
        mAdapter.setListener(object: CourseScheduleClickEvent {
            override fun onClickCourseSchedule(course: CourseEntity) {

            }
        })
        binding.rvCourses.layoutManager = LinearLayoutManager(context)
        binding.rvCourses.adapter = mAdapter
    }

    private fun observeCourses() {
        lifecycleScope.launch {
            mViewModel.courseState.collect {
                when (it) {
                    is CourseState.Idle -> {
                    }
                    is CourseState.Loading -> {
                        binding.tvMessage.visibility = View.INVISIBLE
                        binding.loader.visibility = View.VISIBLE
                        binding.rvCourses.visibility = View.INVISIBLE
                    }
                    is CourseState.Courses -> {
                        binding.tvMessage.visibility = View.INVISIBLE
                        binding.loader.visibility = View.GONE
                        binding.rvCourses.visibility = View.VISIBLE
                        renderCourses(it.course)
                    }
                    is CourseState.Error -> {
                        binding.tvMessage.visibility = View.VISIBLE
                        binding.loader.visibility = View.GONE
                        binding.rvCourses.visibility = View.INVISIBLE
                        Timber.e(it.error?: "Can't load data")
                    }
                }
            }
        }
    }

    private fun renderCourses(cours: List<CourseEntity>) {
        mAdapter.setCourseSchedules(cours.toMutableList())
    }

    fun filter(query: String) {
        mAdapter.filter.filter(query)
    }
}
