package com.workfort.pstuian.app.ui.home.faculty.courseschedule

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
import com.workfort.pstuian.app.data.local.courseschedule.CourseScheduleEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.databinding.FragmentCourseScheduleBinding
import com.workfort.pstuian.app.ui.home.faculty.FacultyActivity
import com.workfort.pstuian.app.ui.home.faculty.adapter.CourseScheduleAdapter
import com.workfort.pstuian.app.ui.home.faculty.listener.CourseScheduleClickEvent
import com.workfort.pstuian.util.remote.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CourseScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = CourseScheduleFragment()
    }

    private lateinit var mBinding: FragmentCourseScheduleBinding
    private lateinit var mViewModel: CourseScheduleViewModel
    private lateinit var mAdapter: CourseScheduleAdapter
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_course_schedule, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mFaculty = (activity as FacultyActivity).mFaculty!!

        mViewModel = ViewModelProviders.of(this).get(CourseScheduleViewModel::class.java)

        mAdapter = CourseScheduleAdapter()
        mAdapter.setListener(object: CourseScheduleClickEvent{
            override fun onClickCourseSchedule(courseSchedule: CourseScheduleEntity) {

            }
        })

        mBinding.rvCourses.layoutManager = LinearLayoutManager(context)
        mBinding.rvCourses.adapter = mAdapter

        mViewModel.getCourseSchedule(mFaculty.shortTitle!!).observe(this, Observer {
            if(it.isEmpty()) {
                loadCourseSchedules(true)
            }else {
                mAdapter.setCourseSchedules(it.toMutableList())

                if(!mTriggeredLoading) {
                    mTriggeredLoading = true
                    loadCourseSchedules(false)
                }
            }
        })
    }

    fun filter(query: String) {
        mAdapter.filter.filter(query)
    }

    private fun loadCourseSchedules(loadFresh: Boolean) {
        if(loadFresh) {
            mBinding.loader.visibility = View.VISIBLE
            mBinding.rvCourses.visibility = View.INVISIBLE
            mBinding.tvMessage.visibility = View.INVISIBLE
        }

        mDisposable.add(apiService.loadCourseSchedules(mFaculty.shortTitle!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(loadFresh) mBinding.loader.visibility = View.INVISIBLE
                if(it.success) {
                    if(it.courseSchedules.isNotEmpty()) {
                        if(loadFresh) mBinding.rvCourses.visibility = View.VISIBLE
                        mViewModel.insertCourseSchedule(it.courseSchedules)
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
