package com.workfort.pstuian.app.ui.employeeprofile

import android.os.Bundle
import android.view.LayoutInflater
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.faculty.adapter.PagerAdapter
import com.workfort.pstuian.app.ui.common.fragment.ProfilePagerItemFragment
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoAction
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoClickEvent
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoItem
import com.workfort.pstuian.databinding.ActivityEmployeeProfileBinding
import com.workfort.pstuian.util.helper.LinkUtil

class EmployeeProfileActivity : BaseActivity<ActivityEmployeeProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityEmployeeProfileBinding
            = ActivityEmployeeProfileBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private lateinit var mFaculty: FacultyEntity
    private lateinit var mEmployee: EmployeeEntity

    private lateinit var pagerAdapter: PagerAdapter

    private val mLinkUtil by lazy { LinkUtil(this) }

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        val faculty = intent.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)
        if(faculty == null) finish()
        else mFaculty = faculty

        val employee = intent.getParcelableExtra<EmployeeEntity>(Const.Key.EMPLOYEE)
        if(employee == null) finish()
        else mEmployee = employee

        setUiData()
        initTabs()

        binding.btnCall.setOnClickListener {
            mEmployee.phone?.let {
                mLinkUtil.callTo(it)
            }
        }
    }

    private fun setUiData() {
        title = ""
        with(binding) {
            if(mEmployee.imageUrl.isNullOrEmpty()) {
                imgAvatar.setImageResource(R.drawable.img_placeholder_profile)
            } else {
                imgAvatar.load(mEmployee.imageUrl) {
                    placeholder(R.drawable.img_placeholder_profile)
                    error(R.drawable.img_placeholder_profile)
                }
            }

            tvName.text = mEmployee.name
        }
    }

    private fun initTabs() {
        pagerAdapter = PagerAdapter(this)
        pagerAdapter.addItem(ProfilePagerItemFragment.instance(getAcademicInfoList()))
        pagerAdapter.addItem(createConnectFragment())
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = pagerAdapter.itemCount

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(when(position) {
                0 -> R.string.txt_academic
                1 -> R.string.txt_connect
                else -> R.string.txt_unknown
            })
        }.attach()
    }

    private fun createConnectFragment() = ProfilePagerItemFragment.instance(getConnectInfoList(),
        object: ProfileInfoClickEvent() {
            override fun onAction(item: ProfileInfoItem) {
                when(item.action) {
                    ProfileInfoAction.CALL -> {
                        mLinkUtil.callTo(item.actionData)
                    }
                    else -> {}
                }
            }
        })

    private fun getAcademicInfoList(): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem(getString(R.string.txt_designation), mEmployee.designation),
            ProfileInfoItem(getString(R.string.txt_department), mEmployee.department?: "~"),
            ProfileInfoItem(getString(R.string.txt_faculty), mFaculty.title),
        )
    }

    private fun getConnectInfoList(): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem(getString(R.string.txt_address), mEmployee.address?: "~"),
            ProfileInfoItem(getString(R.string.txt_phone), mEmployee.phone?: "~",
                R.drawable.ic_call, ProfileInfoAction.CALL, mEmployee.phone?: "~"),
        )
    }
}
