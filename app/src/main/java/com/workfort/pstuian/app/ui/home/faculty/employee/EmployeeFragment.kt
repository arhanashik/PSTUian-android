package com.workfort.pstuian.app.ui.home.faculty.employee

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.databinding.FragmentEmployeesBinding
import com.workfort.pstuian.databinding.PromptEmployeeDetailsBinding
import com.workfort.pstuian.app.ui.home.faculty.FacultyActivity
import com.workfort.pstuian.app.ui.home.faculty.adapter.EmployeeAdapter
import com.workfort.pstuian.app.ui.home.faculty.listener.EmployeeClickEvent
import com.workfort.pstuian.util.helper.ImageLoader
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.remote.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class EmployeeFragment : Fragment() {

    companion object {
        fun newInstance() = EmployeeFragment()
    }

    private lateinit var mBinding: FragmentEmployeesBinding
    private lateinit var mViewModel: EmployeeViewModel
    private lateinit var mAdapter: EmployeeAdapter
    private lateinit var mFaculty: FacultyEntity

    private var mDisposable = CompositeDisposable()
    private val apiService by lazy {
        ApiClient.create()
    }
    private var mTriggeredLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_employees, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mFaculty = (activity as FacultyActivity).mFaculty!!

        mViewModel = ViewModelProviders.of(this).get(EmployeeViewModel::class.java)

        mAdapter = EmployeeAdapter()
        mAdapter.setListener(object: EmployeeClickEvent {
            override fun onClickEmployee(employee: EmployeeEntity) {
                showEmployeeDetails(employee)
            }
        })

        mBinding.rvEmployees.layoutManager = LinearLayoutManager(context)
        mBinding.rvEmployees.adapter = mAdapter

        mViewModel.getEmployees(mFaculty.shortTitle!!).observe(this, Observer {
            if(it.isEmpty()) {
                loadEmployees(true)
            }else {
                mAdapter.setEmployees(it.toMutableList())

                if(!mTriggeredLoading) {
                    mTriggeredLoading = true
                    loadEmployees(false)
                }
            }
        })
    }

    fun filter(query: String) {
        mAdapter.filter.filter(query)
    }

    private fun showEmployeeDetails(employee: EmployeeEntity) {
        val binding = DataBindingUtil.inflate(layoutInflater,
            R.layout.prompt_employee_details, null, false) as PromptEmployeeDetailsBinding

        ImageLoader.load(binding.imgProfile, employee.imageUrl)
        binding.tvName.text = employee.name
        binding.tvDesignation.text = employee.designation
        val department = "Department: " + employee.department
        binding.tvDepartment.text = department
        val faculty = "Faculty: " + employee.faculty
        binding.tvFaculty.text = faculty
        val address = "Address: " + employee.address
        binding.tvAddress.text = address
        val phone = "Phone: " + employee.phone
        binding.tvPhone.text = phone

        val linkUtil = LinkUtil(context!!)
        binding.btnCall.setOnClickListener { linkUtil.callTo(employee.phone!!) }

        val detailsDialog = AlertDialog.Builder(context)
            .setView(binding.root).create()

        detailsDialog.show()
    }

    private fun loadEmployees(loadFresh: Boolean) {
        if(loadFresh) {
            mBinding.loader.visibility = View.VISIBLE
            mBinding.rvEmployees.visibility = View.INVISIBLE
            mBinding.tvMessage.visibility = View.INVISIBLE
        }

        mDisposable.add(apiService.loadEmployees(mFaculty.shortTitle!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(loadFresh) mBinding.loader.visibility = View.INVISIBLE
                if(it.success) {
                    if(it.employees.isNotEmpty()) {
                        if(loadFresh) mBinding.rvEmployees.visibility = View.VISIBLE
                        mViewModel.insertEmployees(it.employees)
                    }else{
                        if(loadFresh) mBinding.tvMessage.visibility = View.VISIBLE
                    }
                }else {
                    if(loadFresh) mBinding.tvMessage.visibility = View.VISIBLE
                }
            }, {
                Timber.e(it)
                if(loadFresh) {
                    mBinding.loader.visibility = View.INVISIBLE
                    mBinding.tvMessage.visibility = View.VISIBLE
                    showToast(it.message.toString())
                }
            })
        )
    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        mDisposable.dispose()
        super.onDestroy()
    }
}
