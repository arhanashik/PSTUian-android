package com.workfort.pstuian.app.ui.teacherprofile

import android.os.Bundle
import android.view.LayoutInflater
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.faculty.adapter.PagerAdapter
import com.workfort.pstuian.app.ui.common.fragment.ProfilePagerItemFragment
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoAction
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoClickEvent
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoItem
import com.workfort.pstuian.databinding.ActivityTeacherProfileBinding
import com.workfort.pstuian.util.helper.LinkUtil

class TeacherProfileActivity : BaseActivity<ActivityTeacherProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTeacherProfileBinding
            = ActivityTeacherProfileBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private lateinit var mFaculty: FacultyEntity
    private lateinit var mTeacher: TeacherEntity

    private lateinit var pagerAdapter: PagerAdapter

    private val mLinkUtil by lazy { LinkUtil(this) }

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        val faculty = intent.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)
        if(faculty == null) finish()
        else mFaculty = faculty

        val teacher = intent.getParcelableExtra<TeacherEntity>(Const.Key.TEACHER)
        if(teacher == null) finish()
        else mTeacher = teacher

        setUiData()
        initTabs()

        binding.btnCall.setOnClickListener {
            mTeacher.phone?.let {
                mLinkUtil.callTo(it)
            }
        }

        binding.btnEmail.setOnClickListener {
            mTeacher.email?.let {
                mLinkUtil.sendEmail(it)
            }
        }
    }

    private fun setUiData() {
        title = ""
        with(binding) {
            if(mTeacher.imageUrl.isNullOrEmpty()) {
                imgAvatar.setImageResource(R.drawable.img_placeholder_profile)
            } else {
                imgAvatar.load(mTeacher.imageUrl) {
                    placeholder(R.drawable.img_placeholder_profile)
                    error(R.drawable.img_placeholder_profile)
                }
            }

            tvName.text = mTeacher.name
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
                    ProfileInfoAction.MAIL -> {
                        mLinkUtil.sendEmail(item.actionData)
                    }
                    ProfileInfoAction.OPEN_LINK -> {
                        mLinkUtil.openBrowser(item.actionData)
                    }
                    else -> {}
                }
            }
        })

    private fun getAcademicInfoList(): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem(getString(R.string.txt_designation), mTeacher.designation),
            ProfileInfoItem(getString(R.string.txt_department), mTeacher.department),
            ProfileInfoItem(getString(R.string.txt_faculty), mFaculty.title),
        )
    }

    private fun getConnectInfoList(): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem(getString(R.string.txt_address), mTeacher.address?: "~"),
            ProfileInfoItem(getString(R.string.txt_phone), mTeacher.phone?: "~",
                R.drawable.ic_call, ProfileInfoAction.CALL, mTeacher.phone?: "~"),
            ProfileInfoItem(getString(R.string.txt_email), mTeacher.email?: "~",
                R.drawable.ic_email, ProfileInfoAction.MAIL, mTeacher.email?: "~"),
            ProfileInfoItem(getString(R.string.txt_linked_in), mTeacher.linkedIn?: "~",
                R.drawable.ic_web, ProfileInfoAction.OPEN_LINK, mTeacher.linkedIn?: "~"),
            ProfileInfoItem(getString(R.string.txt_facebook), mTeacher.fbLink?: "~",
                R.drawable.ic_web, ProfileInfoAction.OPEN_LINK, mTeacher.fbLink?: "~"),
        )
    }
}
