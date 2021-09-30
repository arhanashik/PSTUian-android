package com.workfort.pstuian.app.ui.home.faculty.teachers

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
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.databinding.FragmentTeachersBinding
import com.workfort.pstuian.databinding.PromptTeacherDetailsBinding
import com.workfort.pstuian.app.ui.home.faculty.FacultyActivity
import com.workfort.pstuian.app.ui.home.faculty.adapter.TeachersAdapter
import com.workfort.pstuian.app.ui.home.faculty.listener.TeacherClickEvent
import com.workfort.pstuian.util.helper.ImageLoader
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.remote.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class TeachersFragment : Fragment() {

    companion object {
        fun newInstance() = TeachersFragment()
    }

    private lateinit var mBinding: FragmentTeachersBinding
    private lateinit var mViewModel: TeachersViewModel
    private lateinit var mAdapter: TeachersAdapter
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_teachers, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mFaculty = (activity as FacultyActivity).mFaculty!!

        mViewModel = ViewModelProviders.of(this).get(TeachersViewModel::class.java)

        mAdapter = TeachersAdapter()
        mAdapter.setListener(object: TeacherClickEvent {
            override fun onClickTeacher(teacher: TeacherEntity) {
                showTeacherDetails(teacher)
            }
        })

        mBinding.rvTeachers.layoutManager = LinearLayoutManager(context)
        mBinding.rvTeachers.adapter = mAdapter

        mViewModel.getTeachers(mFaculty.shortTitle!!).observe(this, Observer {
            if(it.isEmpty()) {
                loadTeachers(true)
            }else {
                mAdapter.setTeachers(it.toMutableList())

                if(!mTriggeredLoading) {
                    mTriggeredLoading = true
                    loadTeachers(false)
                }
            }
        })
    }

    fun filter(query: String) {
        mAdapter.filter.filter(query)
    }

    private fun showTeacherDetails(teacher: TeacherEntity) {
        val binding = DataBindingUtil.inflate(layoutInflater,
            R.layout.prompt_teacher_details, null, false) as PromptTeacherDetailsBinding

        ImageLoader.load(binding.imgProfile, teacher.imageUrl)
        binding.tvName.text = teacher.name
        val designationStatus = teacher.designation + "  :  " + teacher.status
        binding.tvDesignationStatus.text = designationStatus
        val department = "Department: " + teacher.department
        binding.tvDepartment.text = department
        val faculty = "Faculty: " + teacher.faculty
        binding.tvFaculty.text = faculty
        val address = "Address: " + teacher.address
        binding.tvAddress.text = address
        val phone = "Phone: " + teacher.phone
        binding.tvPhone.text = phone
        val email = "Email: " + teacher.email
        binding.tvEmail.text = email

        val linkUtil = LinkUtil(context!!)
        binding.btnCall.setOnClickListener { linkUtil.callTo(teacher.phone!!) }
        binding.btnEmail.setOnClickListener { linkUtil.sendEmail(teacher.email!!) }
        binding.tvLinkedIn.setOnClickListener { linkUtil.openBrowser(teacher.linkedIn!!) }
        binding.tvFbLink.setOnClickListener { linkUtil.openBrowser(teacher.fbLink!!) }

        val detailsDialog = AlertDialog.Builder(context)
            .setView(binding.root).create()

        detailsDialog.show()
    }

    private fun loadTeachers(loadFresh: Boolean) {
        if(loadFresh) {
            mBinding.loader.visibility = View.VISIBLE
            mBinding.rvTeachers.visibility = View.INVISIBLE
            mBinding.tvMessage.visibility = View.INVISIBLE
        }

        mDisposable.add(apiService.loadTeachers(mFaculty.shortTitle!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if(loadFresh) mBinding.loader.visibility = View.INVISIBLE
                    if(it.success) {
                        if(it.teachers.isNotEmpty()) {
                            if(loadFresh) mBinding.rvTeachers.visibility = View.VISIBLE
                            mViewModel.insertTeachers(it.teachers)
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
