package com.workfort.pstuian.app.ui.faculty.employee

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
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.base.fragment.BaseFragment
import com.workfort.pstuian.app.ui.faculty.adapter.EmployeeAdapter
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.listener.EmployeeClickEvent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.faculty.viewstate.EmployeeState
import com.workfort.pstuian.databinding.FragmentEmployeesBinding
import com.workfort.pstuian.databinding.PromptEmployeeDetailsBinding
import com.workfort.pstuian.util.helper.LinkUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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

        //initialize the data flow
        mViewModel.handleIntent(faculty.id)

        observeEmployees()
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetEmployees)
        }
    }

    private fun initEmployeeList() {
        mAdapter = EmployeeAdapter()
        mAdapter.setListener(object: EmployeeClickEvent {
            override fun onClickEmployee(employee: EmployeeEntity) {
                showEmployeeDetails(employee)
            }
        })
        binding.rvEmployees.layoutManager = LinearLayoutManager(context)
        binding.rvEmployees.adapter = mAdapter
    }

    private fun observeEmployees() {
        lifecycleScope.launch {
            mViewModel.employeeState.collect {
                when (it) {
                    is EmployeeState.Idle -> {
                    }
                    is EmployeeState.Loading -> {
                        binding.tvMessage.visibility = View.INVISIBLE
                        binding.loader.visibility = View.VISIBLE
                        binding.rvEmployees.visibility = View.INVISIBLE
                    }
                    is EmployeeState.Employees -> {
                        binding.tvMessage.visibility = View.INVISIBLE
                        binding.loader.visibility = View.GONE
                        binding.rvEmployees.visibility = View.VISIBLE
                        renderEmployees(it.employees)
                    }
                    is EmployeeState.Error -> {
                        binding.tvMessage.visibility = View.VISIBLE
                        binding.loader.visibility = View.GONE
                        binding.rvEmployees.visibility = View.INVISIBLE
                        Timber.e(it.error?: "Can't load data")
                    }
                }
            }
        }
    }

    private fun renderEmployees(data: List<EmployeeEntity>) {
        mAdapter.setEmployees(data.toMutableList())
    }

    fun filter(query: String) {
        mAdapter.filter.filter(query)
    }

    private fun showEmployeeDetails(employee: EmployeeEntity) {
        val binding = DataBindingUtil.inflate(layoutInflater,
            R.layout.prompt_employee_details, null, false) as PromptEmployeeDetailsBinding

        with(binding) {
            imgProfile.load(employee.imageUrl)
            tvName.text = employee.name
            tvDesignation.text = employee.designation
            val department = "Department: " + employee.department
            tvDepartment.text = department
            val faculty = "Faculty: " + employee.facultyId
            tvFaculty.text = faculty
            val address = "Address: " + employee.address
            tvAddress.text = address
            val phone = "Phone: " + employee.phone
            tvPhone.text = phone
        }

        val linkUtil = LinkUtil(requireContext())
        binding.btnCall.setOnClickListener { linkUtil.callTo(employee.phone!!) }

        val detailsDialog = AlertDialog.Builder(context)
            .setView(binding.root).create()

        detailsDialog.show()
    }
}