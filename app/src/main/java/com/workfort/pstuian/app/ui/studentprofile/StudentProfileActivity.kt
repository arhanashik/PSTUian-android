package com.workfort.pstuian.app.ui.studentprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import coil.load
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.databinding.ActivityStudentProfileBinding

class StudentProfileActivity : BaseActivity<ActivityStudentProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityStudentProfileBinding
            = ActivityStudentProfileBinding::inflate

    private lateinit var mFaculty: FacultyEntity
    private lateinit var mBatch: BatchEntity
    private lateinit var mStudent: StudentEntity

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        val faculty = intent.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)
        if(faculty == null) finish()
        else mFaculty = faculty

        val batch = intent.getParcelableExtra<BatchEntity>(Const.Key.BATCH)
        if(batch == null) finish()
        else mBatch = batch

        val student = intent.getParcelableExtra<StudentEntity>(Const.Key.STUDENT)
        if(student == null) finish()
        else mStudent = student

        setHomeEnabled()
        title = ""
        setUiData()
    }

    private fun setUiData() {
        with(binding) {
            if(mStudent.imageUrl.isNullOrEmpty()) {
                imgAvatar.setImageResource(R.drawable.img_placeholder_profile)
            } else {
                imgAvatar.load(mStudent.imageUrl) {
                    placeholder(R.drawable.img_placeholder_profile)
                    error(R.drawable.img_placeholder_profile)
                }
            }
            tvName.text = mStudent.name
            tvId.text = mStudent.id.toString()
            tvReg.text = mStudent.reg
            val blood = if(mStudent.blood.isNullOrEmpty()) "~Not available" else mStudent.blood
            tvBloodGroup.text = blood
            tvFaculty.text = mFaculty.title
            tvSession.text = mBatch.session
            tvBatch.text = mBatch.name
            val address = if(mStudent.address.isNullOrEmpty()) "~Not available" else mStudent.address
            tvAddress.text = address
            val phone = if(mStudent.phone.isNullOrEmpty()) "~Not available" else mStudent.phone
            tvPhone.text = phone
            val email = if(mStudent.email.isNullOrEmpty()) "~Not available" else mStudent.email
            tvEmail.text = email
            val cvLink = if(mStudent.cvLink.isNullOrEmpty()) "~Not available" else mStudent.cvLink
            tvCv.text = cvLink
            val linkedIn = if(mStudent.linkedIn.isNullOrEmpty()) "~Not available" else mStudent.linkedIn
            tvLinkedIn.text = linkedIn
            val fbLink = if(mStudent.fbLink.isNullOrEmpty()) "~Not available" else mStudent.fbLink
            tvFacebook.text = fbLink

            if(mStudent.email.isNullOrEmpty()) btnEmail.visibility = View.INVISIBLE
            if(mStudent.cvLink.isNullOrEmpty()) btnDownloadCv.visibility = View.INVISIBLE
        }
    }
}
