package com.workfort.pstuian.app.ui.teacherprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import coil.load
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.databinding.ActivityTeacherProfileBinding

class TeacherProfileActivity : BaseActivity<ActivityTeacherProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTeacherProfileBinding
            = ActivityTeacherProfileBinding::inflate

    private lateinit var mFaculty: FacultyEntity
    private lateinit var mTeacher: TeacherEntity

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        val faculty = intent.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)
        if(faculty == null) finish()
        else mFaculty = faculty

        val teacher = intent.getParcelableExtra<TeacherEntity>(Const.Key.TEACHER)
        if(teacher == null) finish()
        else mTeacher = teacher

        setHomeEnabled()
        title = ""
        setUiData()
    }

    private fun setUiData() {
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
            tvDesignation.text = mTeacher.designation
            tvDepartment.text = mTeacher.department
            tvFaculty.text = mFaculty.title
            val address = if(mTeacher.address.isNullOrEmpty()) "~Not available" else mTeacher.address
            tvAddress.text = address
            val phone = if(mTeacher.phone.isNullOrEmpty()) "~Not available" else mTeacher.phone
            tvPhone.text = phone
            val email = if(mTeacher.email.isNullOrEmpty()) "~Not available" else mTeacher.email
            tvEmail.text = email
            val linkedIn = if(mTeacher.linkedIn.isNullOrEmpty()) "~Not available" else mTeacher.linkedIn
            tvLinkedIn.text = linkedIn
            val fbLink = if(mTeacher.fbLink.isNullOrEmpty()) "~Not available" else mTeacher.fbLink
            tvFacebook.text = fbLink

            if(mTeacher.email.isNullOrEmpty()) btnEmail.visibility = View.INVISIBLE
        }
    }
}
