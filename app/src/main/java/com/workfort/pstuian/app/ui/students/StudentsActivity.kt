package com.workfort.pstuian.app.ui.students

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.listener.StudentClickEvent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.studentprofile.StudentProfileActivity
import com.workfort.pstuian.app.ui.students.adapter.StudentsAdapter
import com.workfort.pstuian.app.ui.students.viewstate.StudentsState
import com.workfort.pstuian.databinding.ActivityStudentsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentsActivity : BaseActivity<ActivityStudentsBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityStudentsBinding
            = ActivityStudentsBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar
    override fun getMenuId(): Int = R.menu.menu_search
    override fun getSearchMenuItemId(): Int = R.id.action_search
    override fun getSearchQueryHint(): String = getString(R.string.hint_search_student)

    override fun observeBroadcast() = Const.IntentAction.NOTIFICATION
    override fun onBroadcastReceived(intent: Intent) {
        handleNotificationIntent(intent)
    }

    private val mViewModel: FacultyViewModel by viewModel()
    private lateinit var mAdapter: StudentsAdapter
    private lateinit var mFaculty: FacultyEntity
    private lateinit var mBatch: BatchEntity

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        val faculty = intent.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)
        if(faculty == null) finish()
        else mFaculty = faculty

        val batch = intent.getParcelableExtra<BatchEntity>(Const.Key.BATCH)
        if(batch == null) finish()
        else mBatch = batch

        binding.toolbar.title = mBatch.name
        initStudentsList()
        observeStudents()
        //initialize the data flow
        mViewModel.facultyId = mFaculty.id
        mViewModel.batchId = mBatch.id
        loadData()
        with(binding) {
            srlReloadData.setOnRefreshListener { loadData() }
            btnRefresh.setOnClickListener { loadData() }
        }
    }

    override fun onSearchQueryChange(searchQuery: String) {
        mAdapter.filter.filter(searchQuery)
    }

    private fun initStudentsList() {
        mAdapter = StudentsAdapter()
        mAdapter.setListener(object: StudentClickEvent {
            override fun onClickStudent(student: StudentEntity) {
                gotToStudentProfile(mFaculty, mBatch, student)
            }
        })
        binding.rvData.adapter = mAdapter
    }

    private fun loadData(forceRefresh: Boolean = false) {
        mViewModel.studentsStateForceRefresh = forceRefresh
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetStudents)
        }
    }

    private fun observeStudents() {
        lifecycleScope.launch {
            mViewModel.studentsState.collect {
                when (it) {
                    is StudentsState.Idle -> Unit
                    is StudentsState.Loading -> setActionUiState(true)
                    is StudentsState.Students -> {
                        setActionUiState(false)
                        renderStudents(it.students)
                    }
                    is StudentsState.Error -> {
                        setActionUiState(false)
                        renderStudents(emptyList())
                        binding.tvMessage.text = it.error?: "Can't load data"
                    }
                }
            }
        }
    }

    private fun setActionUiState(isLoading: Boolean) {
        val visibility = if(isLoading) View.VISIBLE else View.GONE
        val inverseVisibility = if(isLoading) View.GONE else View.VISIBLE
        with(binding) {
            srlReloadData.isRefreshing = isLoading
            shimmerLayout.visibility = visibility
            if(isLoading) shimmerLayout.startShimmer()
            else shimmerLayout.stopShimmer()

            rvData.visibility = inverseVisibility
            lavError.visibility = inverseVisibility
            tvMessage.visibility = inverseVisibility
            btnRefresh.visibility = inverseVisibility
        }
    }

    private fun renderStudents(data: List<StudentEntity>) {
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
        mAdapter.setStudents(data.toMutableList())
    }

    private fun gotToStudentProfile(
        faculty: FacultyEntity,
        batch: BatchEntity,
        student: StudentEntity
    ) {
        val intent = Intent(this, StudentProfileActivity::class.java)
        intent.putExtra(Const.Key.FACULTY, faculty)
        intent.putExtra(Const.Key.BATCH, batch)
        intent.putExtra(Const.Key.STUDENT, student)
        startActivityForResult.launch(intent)
    }

    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            loadData()
        }
    }
}
