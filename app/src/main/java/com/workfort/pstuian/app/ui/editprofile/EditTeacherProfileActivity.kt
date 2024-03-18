package com.workfort.pstuian.app.ui.editprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.dialog.CommonDialog
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.teacherprofile.viewmodel.TeacherProfileViewModel
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.databinding.ActivityEditTeacherProfileBinding
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.util.helper.Toaster
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EditTeacherProfileActivity: BaseActivity<ActivityEditTeacherProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityEditTeacherProfileBinding
            = ActivityEditTeacherProfileBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private val mViewModel: TeacherProfileViewModel by viewModel()
    private val mFacultyViewModel: FacultyViewModel by viewModel()
    private lateinit var mTeacher: TeacherEntity
    private lateinit var mEditAction: String
    private var mFaculties = ArrayList<FacultyEntity>()

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        val teacher = intent.getParcelableExtra<TeacherEntity>(Const.Key.TEACHER)
        if(teacher == null) finish()
        else mTeacher = teacher

        mEditAction = intent.getStringExtra(Const.Key.EDIT_ACTION)?: Const.Key.EDIT_ACADEMIC
        setUiData()

        observeFaculties()
        observeAcademicInfoChange()
        observeConnectInfoChange()
        if(mEditAction == Const.Key.EDIT_ACADEMIC) {
            mFacultyViewModel.facultyId = mTeacher.facultyId
            lifecycleScope.launch {
                mFacultyViewModel.intent.send(FacultyIntent.GetFaculties)
            }
        }
    }

    private fun setUiData() {
        with(binding.content) {
            if(mEditAction == Const.Key.EDIT_ACADEMIC) {
                groupAcademicInfo.visibility = View.VISIBLE
                groupConnect.visibility = View.GONE
                etName.setText(mTeacher.name)
                etName.setSelection(mTeacher.name.length)
                etDesignation.setText(mTeacher.designation)
                etDepartment.setText(mTeacher.department)
                //set blood group dropdown
                val groups = resources.getStringArray(R.array.blood_group)
                val bloodGroupAdapter = ArrayAdapter(this@EditTeacherProfileActivity,
                    R.layout.row_dropdown_item, groups)
                etBlood.setAdapter(bloodGroupAdapter)
                if(!mTeacher.blood.isNullOrEmpty()) {
                    etBlood.setText(mTeacher.blood, false)
                }

                val facultyChangeWatcher = object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        p0?.toString().let { selectedFaculty ->
                            mFaculties.filter { it.shortTitle == selectedFaculty }.also {
                                if(it.isNotEmpty()) {
                                    mFacultyViewModel.facultyId = it[0].id
                                }
                            }
                        }
                    }
                }
                etFaculty.addTextChangedListener(facultyChangeWatcher)
            } else {
                groupAcademicInfo.visibility = View.GONE
                groupConnect.visibility = View.VISIBLE
                etAddress.setText(mTeacher.address)
                etPhone.setText(mTeacher.phone)
                etEmail.setText(mTeacher.email)
                etLinkedIn.setText(mTeacher.linkedIn)
                etFacebook.setText(mTeacher.fbLink)
                // for now email is not editable
                tilEmail.isEnabled = false
            }
        }
        with(binding) {
            lifecycleScope.launch {
                delay(1000)
                if(btnSaveChanges.isExtended) btnSaveChanges.shrink()
            }
            btnSaveChanges.setOnClickListener {
                if(mEditAction == Const.Key.EDIT_ACADEMIC) {
                    updateAcademicInfo()
                } else {
                    updateConnectInfo()
                }
            }
        }
    }

    private fun observeFaculties() {
        lifecycleScope.launch {
            mFacultyViewModel.facultyState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUiState(isActionRunning = true)
                    is RequestState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        val faculties = it.data as List<FacultyEntity>
                        mFaculties.clear()
                        mFaculties.addAll(faculties)
                        renderFaculties(faculties)
                    }
                    is RequestState.Error -> {
                        setActionUiState(isActionRunning = false)
                        Timber.e("Can't load data")
                    }
                }
            }
        }
    }

    private fun renderFaculties(items: List<FacultyEntity>) {
        val selectedFaculty = getSelectedFacultyEntity()?.shortTitle?: ""
        val faculties = items.map { faculty -> faculty.shortTitle }
        val facultyAdapter = ArrayAdapter(this@EditTeacherProfileActivity,
            R.layout.row_dropdown_item, faculties)
        with(binding.content) {
            etFaculty.setAdapter(facultyAdapter)
            etFaculty.setText(selectedFaculty, false)
        }
    }

    private fun getSelectedFacultyEntity() : FacultyEntity? {
        mFaculties.filter { it.id == mTeacher.facultyId }.also {
            if(it.isNotEmpty()) {
                return it[0]
            }
        }

        return null
    }

    private fun setActionUiState(isActionRunning: Boolean, isLoaderIndeterminate: Boolean = true) {
        val viewVisibility = if(isActionRunning) View.VISIBLE else View.GONE
        with(binding) {
            loaderOverlay.visibility = viewVisibility
            labelSaving.visibility = viewVisibility
            loader.visibility = View.GONE
            if(isActionRunning) {
                loader.isIndeterminate = isLoaderIndeterminate
                loader.visibility = View.VISIBLE
            }
        }
    }

    private fun updateAcademicInfo() {
        with(binding.content) {
            val name = etName.text.toString().trim()
            if(TextUtils.isEmpty(name)) {
                tilName.error = "*Required"
                return
            }
            tilName.error = null

            val designation = etDesignation.text.toString().trim()
            if(TextUtils.isEmpty(designation)) {
                tilDesignation.error = "*Required"
                return
            }
            tilDesignation.error = null

            val department = etDepartment.text.toString().trim()
            if(TextUtils.isEmpty(department)) {
                tilDepartment.error = "*Required"
                return
            }
            tilDepartment.error = null

            val blood = etBlood.text.toString()

            val faculty = etFaculty.text.toString()
            if(TextUtils.isEmpty(faculty)) {
                tilFaculty.error = "*Required"
                return
            }
            tilFaculty.error = null

            var facultyId = -1
            mFaculties.filter { it.shortTitle == faculty }.also {
                if(it.isNotEmpty()) {
                    facultyId = it[0].id
                }
            }
            if(facultyId == -1) {
                Toaster.show("Invalid faculty")
                return
            }

            mViewModel.changeAcademicInfo(
                mTeacher, name, designation, department, blood, facultyId
            )
        }
    }

    private fun observeAcademicInfoChange() {
        lifecycleScope.launch {
            mViewModel.changeAcademicInfoState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is RequestState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        mTeacher = it.data as TeacherEntity
                        getSelectedFacultyEntity()?.let { faculty ->
                            setActivityResult(mTeacher, faculty)
                        }
                        CommonDialog.success(this@EditTeacherProfileActivity)
                    }
                    is RequestState.Error -> {
                        setActionUiState(isActionRunning = false)
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@EditTeacherProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    private fun updateConnectInfo() {
        with(binding.content) {
            val address = etAddress.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val email = etEmail.text.toString().trim()
            if(TextUtils.isEmpty(email)) {
                tilEmail.error = "*Required"
                return
            }
            tilEmail.error = null
            val linkedIn = etLinkedIn.text.toString().trim()
            val fbLink = etFacebook.text.toString().trim()

            mViewModel.changeConnectInfo(
                mTeacher, address, phone, email, linkedIn, fbLink
            )
        }
    }

    private fun observeConnectInfoChange() {
        lifecycleScope.launch {
            mViewModel.changeConnectInfoState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is RequestState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        mTeacher = it.data as TeacherEntity
                        setActivityResult(mTeacher)
                        CommonDialog.success(this@EditTeacherProfileActivity)
                    }
                    is RequestState.Error -> {
                        setActionUiState(isActionRunning = false)
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@EditTeacherProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    private fun setActivityResult(
        teacher: TeacherEntity,
        faculty: FacultyEntity? = null,
        dataChanged: Boolean = true
    ) {
        val intent = Intent()
        intent.putExtra(Const.Key.TEACHER, teacher)
        intent.putExtra(Const.Key.FACULTY, faculty)
        intent.putExtra(Const.Key.UPDATED, dataChanged)
        setResult(Activity.RESULT_OK, intent)
    }
}
