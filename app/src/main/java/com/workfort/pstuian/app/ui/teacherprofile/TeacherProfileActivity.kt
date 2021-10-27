package com.workfort.pstuian.app.ui.teacherprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import com.workfort.pstuian.app.ui.webview.WebViewActivity
import com.workfort.pstuian.databinding.ActivityTeacherProfileBinding
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.Toaster

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
    }

    private fun setUiData() {
        title = mTeacher.name
        with(binding) {
            if(mTeacher.imageUrl.isNullOrEmpty()) {
                imgAvatar.setImageResource(R.drawable.img_placeholder_profile)
            } else {
                imgAvatar.load(mTeacher.imageUrl) {
                    placeholder(R.drawable.img_placeholder_profile)
                    error(R.drawable.img_placeholder_profile)
                }
            }
            if(mTeacher.bio.isNullOrEmpty()) {
                tvBio.visibility = View.GONE
            } else {
                tvBio.visibility = View.VISIBLE
                tvBio.text = mTeacher.bio
            }

            btnCall.setOnClickListener { promptCall(mTeacher.phone) }
            btnEmail.setOnClickListener { promptEmail(mTeacher.email) }
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
                    ProfileInfoAction.CALL -> { promptCall(item.actionData) }
                    ProfileInfoAction.MAIL -> { promptEmail(item.actionData) }
                    ProfileInfoAction.OPEN_LINK -> item.actionData?.let { openLink(it) }
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
                R.drawable.ic_call, ProfileInfoAction.CALL, mTeacher.phone),
            ProfileInfoItem(getString(R.string.txt_email), mTeacher.email?: "~",
                R.drawable.ic_email, ProfileInfoAction.MAIL, mTeacher.email),
            ProfileInfoItem(getString(R.string.txt_linked_in), mTeacher.linkedIn?: "~",
                R.drawable.ic_web, ProfileInfoAction.OPEN_LINK, mTeacher.linkedIn),
            ProfileInfoItem(getString(R.string.txt_facebook), mTeacher.fbLink?: "~",
                R.drawable.ic_web, ProfileInfoAction.OPEN_LINK, mTeacher.fbLink),
        )
    }

    private fun promptCall(phoneNumber: String?) {
        if(phoneNumber.isNullOrEmpty()) {
            Toaster.show(R.string.txt_error_call)
            return
        }
        val msg = "${getString(R.string.txt_msg_call)} $phoneNumber"
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.txt_title_call)
            .setMessage(msg)
            .setPositiveButton(R.string.txt_call) { _,_ ->
                mLinkUtil.callTo(phoneNumber)
            }
            .setNegativeButton(R.string.txt_dismiss) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun promptEmail(email: String?) {
        if(email.isNullOrEmpty()) {
            Toaster.show(R.string.txt_error_email)
            return
        }
        val msg = "${getString(R.string.txt_msg_email)} $email"
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.txt_title_email)
            .setMessage(msg)
            .setPositiveButton(R.string.txt_send) { _,_ ->
                mLinkUtil.sendEmail(email)
            }
            .setNegativeButton(R.string.txt_dismiss) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun openLink(link: String) {
        if(!link.startsWith("http://") || link.startsWith("https://")) {
            Toaster.show("Invalid link")
            return
        }

        if(link.contains("linkedin.com") || link.contains("facebook.com")) {
            mLinkUtil.openBrowser(link)
        } else {
            launchActivity<WebViewActivity>(Pair(Const.Key.URL, link))
        }
    }
}
