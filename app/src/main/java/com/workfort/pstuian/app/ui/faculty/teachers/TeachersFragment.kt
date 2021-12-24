package com.workfort.pstuian.app.ui.faculty.teachers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
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
        loadData()
        with(binding) {
            srlReloadData.setOnRefreshListener { loadData(true) }
            btnRefresh.setOnClickListener { loadData(true) }
        }
    }

    private fun initTeacherList() {
        mAdapter = TeachersAdapter()
        mAdapter.setListener(object: TeacherClickEvent {
            override fun onClickTeacher(teacher: TeacherEntity) {
                gotToTeacherProfile(teacher)
            }
        })
        binding.rvData.adapter = mAdapter
    }

    private fun loadData(forceRefresh: Boolean = false) {
        mViewModel.teacherStateForceRefresh = forceRefresh
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetTeachers)
        }
    }

    private fun observeTeachers() {
        lifecycleScope.launch {
            mViewModel.teacherState.collect {
                when (it) {
                    is TeacherState.Idle -> Unit
                    is TeacherState.Loading -> setActionUiState(true)
                    is TeacherState.Teachers -> {
                        setActionUiState(false)
                        renderTeachers(it.teachers)
                    }
                    is TeacherState.Error -> {
                        setActionUiState(false)
                        renderTeachers(emptyList())
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

    private fun renderTeachers(data: List<TeacherEntity>) {
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

    private fun gotToTeacherProfile(teacher: TeacherEntity) {
        val intent = Intent(requireContext(), TeacherProfileActivity::class.java)
        intent.putExtra(Const.Key.TEACHER_ID, teacher.id)
        startActivityForResult.launch(intent)
    }

    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            loadData()
        }
    }
}
