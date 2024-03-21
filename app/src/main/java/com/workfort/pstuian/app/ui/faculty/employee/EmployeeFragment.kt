package com.workfort.pstuian.app.ui.faculty.employee

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.app.ui.base.fragment.BaseFragment
import com.workfort.pstuian.app.ui.employeeprofile.EmployeeProfileActivity
import com.workfort.pstuian.app.ui.faculty.adapter.EmployeeAdapter
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.listener.EmployeeClickEvent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.databinding.FragmentEmployeesBinding
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.RequestState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmployeeFragment(private val faculty: FacultyEntity)
    : BaseFragment<FragmentEmployeesBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEmployeesBinding
            = FragmentEmployeesBinding::inflate

    companion object {
        fun newInstance(faculty: FacultyEntity) = EmployeeFragment(faculty)
    }

    private val mViewModel: FacultyViewModel by viewModel()
    private lateinit var mAdapter: EmployeeAdapter

    override fun afterOnViewCreated(view: View, savedInstanceState: Bundle?) {
        initEmployeeList()
        observeEmployees()
        mViewModel.facultyId = faculty.id
        loadData()
        with(binding) {
            srlReloadData.setOnRefreshListener { loadData(true) }
            btnRefresh.setOnClickListener { loadData(true) }
        }
    }

    private fun initEmployeeList() {
        mAdapter = EmployeeAdapter()
        mAdapter.setListener(object: EmployeeClickEvent {
            override fun onClickEmployee(employee: EmployeeEntity) {
                val intent = Intent(context, EmployeeProfileActivity::class.java)
                intent.putExtra(Const.Key.FACULTY, faculty)
                intent.putExtra(Const.Key.EMPLOYEE, employee)
                startActivity(intent)
            }
        })
        binding.rvData.adapter = mAdapter
    }

    private fun loadData(forceRefresh: Boolean = false) {
        mViewModel.employeeStateForceRefresh = forceRefresh
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetEmployees)
        }
    }

    private fun observeEmployees() {
        lifecycleScope.launch {
            mViewModel.employeeState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading ->  setActionUiState(true)
                    is RequestState.Success<*> -> {
                        setActionUiState(false)
                        renderEmployees(it.data as List<EmployeeEntity>)
                    }
                    is RequestState.Error -> {
                        setActionUiState(false)
                        renderEmployees(emptyList())
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

    private fun renderEmployees(data: List<EmployeeEntity>) {
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
