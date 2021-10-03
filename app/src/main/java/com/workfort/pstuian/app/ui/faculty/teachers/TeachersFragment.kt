package com.workfort.pstuian.app.ui.faculty.teachers

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.ui.base.fragment.BaseFragment
import com.workfort.pstuian.app.ui.faculty.adapter.TeachersAdapter
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.listener.TeacherClickEvent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.faculty.viewstate.TeacherState
import com.workfort.pstuian.databinding.FragmentTeachersBinding
import com.workfort.pstuian.databinding.PromptTeacherDetailsBinding
import com.workfort.pstuian.util.helper.LinkUtil
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

        //initialize the data flow
        mViewModel.handleIntent(faculty.id)

        observeTeachers()
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetTeachers)
        }
    }

    private fun initTeacherList() {
        mAdapter = TeachersAdapter()
        mAdapter.setListener(object: TeacherClickEvent {
            override fun onClickTeacher(teacher: TeacherEntity) {
                showTeacherDetails(teacher)
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

    private fun showTeacherDetails(teacher: TeacherEntity) {
        val binding = DataBindingUtil.inflate(layoutInflater,
            R.layout.prompt_teacher_details, null, false) as PromptTeacherDetailsBinding

        val linkUtil = LinkUtil(requireContext())
        with(binding) {
            imgProfile.load(teacher.imageUrl)
            tvName.text = teacher.name
            val designationStatus = teacher.designation + "  :  " + teacher.status
            tvDesignationStatus.text = designationStatus
            val department = "Department: " + teacher.department
            tvDepartment.text = department
            val faculty = "Faculty: " + teacher.facultyId
            tvFaculty.text = faculty
            val address = "Address: " + teacher.address
            tvAddress.text = address
            val phone = "Phone: " + teacher.phone
            tvPhone.text = phone
            val email = "Email: " + teacher.email
            tvEmail.text = email

            btnCall.setOnClickListener { linkUtil.callTo(teacher.phone?: "") }
            btnEmail.setOnClickListener { linkUtil.sendEmail(teacher.email?: "") }
            tvLinkedIn.setOnClickListener { linkUtil.openBrowser(teacher.linkedIn?: "") }
            tvFbLink.setOnClickListener { linkUtil.openBrowser(teacher.fbLink?: "") }
        }

        val detailsDialog = AlertDialog.Builder(context)
            .setView(binding.root).create()

        detailsDialog.show()
    }
}
