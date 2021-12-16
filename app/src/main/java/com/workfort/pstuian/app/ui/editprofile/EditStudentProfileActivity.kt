package com.workfort.pstuian.app.ui.editprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.viewmodel.FileHandlerViewModel
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.faculty.viewstate.BatchesState
import com.workfort.pstuian.app.ui.faculty.viewstate.FacultyState
import com.workfort.pstuian.app.ui.studentprofile.intent.StudentProfileIntent
import com.workfort.pstuian.app.ui.studentprofile.viewmodel.StudentProfileViewModel
import com.workfort.pstuian.app.ui.studentprofile.viewstate.ChangeProfileInfoState
import com.workfort.pstuian.databinding.ActivityEditStudentProfileBinding
import com.workfort.pstuian.util.helper.PermissionUtil
import com.workfort.pstuian.util.helper.Toaster
import com.workfort.pstuian.util.helper.nameFilter
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EditStudentProfileActivity: BaseActivity<ActivityEditStudentProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityEditStudentProfileBinding
            = ActivityEditStudentProfileBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private val mStudentViewModel: StudentProfileViewModel by viewModel()
    private val mFacultyViewModel: FacultyViewModel by viewModel()
    private val mFileHandlerViewModel: FileHandlerViewModel by viewModel()
    private lateinit var mStudent: StudentEntity
    private lateinit var mEditAction: String
    private var mFaculties = ArrayList<FacultyEntity>()
    private var mBatches = ArrayList<BatchEntity>()

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        val student = intent.getParcelableExtra<StudentEntity>(Const.Key.STUDENT)
        if(student == null) finish()
        else mStudent = student

        mEditAction = intent.getStringExtra(Const.Key.EDIT_ACTION)?: Const.Key.EDIT_ACADEMIC
        setUiData()

        observeFaculties()
        observeBatches()
        observeAcademicInfoChange()
        observeConnectInfoChange()
        if(mEditAction == Const.Key.EDIT_ACADEMIC) {
            mFacultyViewModel.facultyId = mStudent.facultyId
            lifecycleScope.launch {
                mFacultyViewModel.intent.send(FacultyIntent.GetFaculties)
                mFacultyViewModel.intent.send(FacultyIntent.GetBatches)
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Const.RequestCode.PICK_PDF && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                cvChangeDialog.let { dialog ->
                    if(dialog != null && dialog.isShowing) dialog.dismiss()
                    promptChangeCV(uri)
                }
            }
        }
    }

    private fun setUiData() {
        with(binding.content) {
            if(mEditAction == Const.Key.EDIT_ACADEMIC) {
                groupAcademicInfo.visibility = View.VISIBLE
                groupConnect.visibility = View.GONE
                etName.setText(mStudent.name)
                etName.setSelection(mStudent.name.length)
                etName.filters = arrayOf(nameFilter)
                etId.setText(mStudent.id.toString())
                etReg.setText(mStudent.reg)
                etSession.setText(mStudent.session)
                //set blood group dropdown
                val groups = resources.getStringArray(R.array.blood_group)
                val bloodGroupAdapter = ArrayAdapter(this@EditStudentProfileActivity,
                    R.layout.row_dropdown_item, groups)
                etBlood.setAdapter(bloodGroupAdapter)
                if(!mStudent.blood.isNullOrEmpty()) {
                    etBlood.setText(mStudent.blood, false)
                }

                val facultyChangeWatcher = object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        p0?.toString().let { selectedFaculty ->
                            mFaculties.filter { it.shortTitle == selectedFaculty }.also {
                                if(it.isNotEmpty()) {
                                    mFacultyViewModel.facultyId = it[0].id
                                    lifecycleScope.launch {
                                        mFacultyViewModel.intent.send(FacultyIntent.GetBatches)
                                    }
                                }
                            }
                        }
                    }
                }
                etFaculty.addTextChangedListener(facultyChangeWatcher)
            } else {
                groupAcademicInfo.visibility = View.GONE
                groupConnect.visibility = View.VISIBLE
                etAddress.setText(mStudent.address)
                etPhone.setText(mStudent.phone)
                etEmail.setText(mStudent.email)
                etCv.setText(mStudent.cvLink)
                etLinkedIn.setText(mStudent.linkedIn)
                etFacebook.setText(mStudent.fbLink)
                btnUploadCv.setOnClickListener { choosePdf() }
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
                    is FacultyState.Idle -> {
                    }
                    is FacultyState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is FacultyState.Faculties -> {
                        setActionUiState(isActionRunning = false)
                        mFaculties.clear()
                        mFaculties.addAll(it.faculties)
                        renderFaculties(it.faculties)
                    }
                    is FacultyState.Error -> {
                        setActionUiState(isActionRunning = false)
                        Timber.e("Can't load data")
                    }
                }
            }
        }
    }

    private fun renderFaculties(items: List<FacultyEntity>) {
        var selectedFaculty = ""
        items.filter { it.id == mStudent.facultyId }.also {
            if(it.isNotEmpty()) selectedFaculty = it[0].shortTitle
        }
        val faculties = items.map { faculty -> faculty.shortTitle }
        val facultyAdapter = ArrayAdapter(this@EditStudentProfileActivity,
            R.layout.row_dropdown_item, faculties)
        with(binding.content) {
            etFaculty.setAdapter(facultyAdapter)
            etFaculty.setText(selectedFaculty, false)
        }
    }

    private fun observeBatches() {
        lifecycleScope.launch {
            mFacultyViewModel.batchesState.collect {
                when (it) {
                    is BatchesState.Idle -> {
                    }
                    is BatchesState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is BatchesState.Batches -> {
                        setActionUiState(isActionRunning = false)
                        mBatches.clear()
                        mBatches.addAll(it.batches)
                        renderBatches(it.batches)
                    }
                    is BatchesState.Error -> {
                        setActionUiState(isActionRunning = false)
                        renderBatches(emptyList())
                        Timber.e("Can't load data")
                    }
                }
            }
        }
    }

    private fun renderBatches(items: List<BatchEntity>) {
        var selectedBatch = ""
        items.filter { it.id == mStudent.batchId }.also {
            if(it.isNotEmpty()) selectedBatch = it[0].name
        }
        val batches = items.map { batch -> batch.name }
        val adapter = ArrayAdapter(this@EditStudentProfileActivity,
            R.layout.row_dropdown_item, batches)
        with(binding.content) {
            etBatch.setAdapter(adapter)
            etBatch.setText(selectedBatch, false)
        }
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
            val name = etName.text.toString()
            if(TextUtils.isEmpty(name)) {
                tilName.error = "*Required"
                return
            }
            tilName.error = null

            val idStr = etId.text.toString()
            if(TextUtils.isEmpty(idStr)) {
                tilId.error = "*Required"
                return
            }
            val id = idStr.toIntOrNull()
            if(id == null) {
                tilId.error = "*Invalid Id"
                return
            }
            tilId.error = null

            val reg = etReg.text.toString()
            if(TextUtils.isEmpty(reg)) {
                tilReg.error = "*Required"
                return
            }
            if(reg.toIntOrNull() == null) {
                tilReg.error = "*Invalid Reg"
                return
            }
            tilReg.error = null

            val blood = etBlood.text.toString()

            val faculty = etFaculty.text.toString()
            if(TextUtils.isEmpty(faculty)) {
                tilFaculty.error = "*Required"
                return
            }
            tilFaculty.error = null

            val session = etSession.text.toString()
            if(TextUtils.isEmpty(session)) {
                tilSession.error = "*Required"
                return
            }
            tilSession.error = null

            val batch = etBatch.text.toString()
            if(TextUtils.isEmpty(batch)) {
                tilBatch.error = "*Required"
                return
            }
            tilBatch.error = null

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

            var batchId = -1
            mBatches.filter { it.name == batch }.also {
                if(it.isNotEmpty()) {
                    batchId = it[0].id
                }
            }
            if(batchId == -1) {
                Toaster.show("Invalid batch")
                return
            }

            lifecycleScope.launch {
                mStudentViewModel.intent.send(StudentProfileIntent.ChangeAcademicInfo(
                    mStudent, name, id, reg, blood, facultyId, session, batchId
                ))
            }
        }
    }

    private fun observeAcademicInfoChange() {
        lifecycleScope.launch {
            mStudentViewModel.changeAcademicInfoState.collect {
                when (it) {
                    is ChangeProfileInfoState.Idle -> {
                    }
                    is ChangeProfileInfoState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is ChangeProfileInfoState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        if(it.data is StudentEntity) mStudent = it.data
                        setActivityResult()
                        CommonDialog.success(this@EditStudentProfileActivity)
                    }
                    is ChangeProfileInfoState.Error -> {
                        setActionUiState(isActionRunning = false)
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@EditStudentProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    private fun updateConnectInfo() {
        with(binding.content) {
            val address = etAddress.text.toString()
            val phone = etPhone.text.toString()
            val email = etEmail.text.toString()
            if(TextUtils.isEmpty(email)) {
                tilEmail.error = "*Required"
                return
            }
            tilEmail.error = null
            val cvLink = etCv.text.toString()
            val linkedIn = etLinkedIn.text.toString()
            val facebook = etFacebook.text.toString()

            lifecycleScope.launch {
                mStudentViewModel.intent.send(StudentProfileIntent.ChangeConnectInfo(
                    mStudent, address, phone, email, cvLink, linkedIn, facebook
                ))
            }
        }
    }

    private fun observeConnectInfoChange() {
        lifecycleScope.launch {
            mStudentViewModel.changeConnectInfoState.collect {
                when (it) {
                    is ChangeProfileInfoState.Idle -> {
                    }
                    is ChangeProfileInfoState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is ChangeProfileInfoState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        if(it.data is StudentEntity) mStudent = it.data
                        setActivityResult()
                        CommonDialog.success(this@EditStudentProfileActivity)
                    }
                    is ChangeProfileInfoState.Error -> {
                        setActionUiState(isActionRunning = false)
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@EditStudentProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun choosePdf() {
        if(!PermissionUtil.isAllowed(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            return
        }
        val pickIntent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }
        startActivityForResult(pickIntent, Const.RequestCode.PICK_PDF)
    }

    private val permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if(isGranted) {
            choosePdf()
        } else {
            Toaster.show("Storage permission is necessary")
        }
    }

    private var cvChangeDialog: AlertDialog? = null
    private fun promptChangeCV(pdfUri: Uri) {
        cvChangeDialog = CommonDialog.changeCV(this, pdfUri, {
            choosePdf()
        }, {
            cvChangeDialog?.dismiss()
            val pdfFileName = "cv_${mStudent.id}.pdf"
            uploadPdf(pdfUri, pdfFileName)
        })
    }

    private fun uploadPdf(pdfUri: Uri, fileName: String) {
        mFileHandlerViewModel.uploadPdf(this, pdfUri, fileName).observe(this, {
            when (it.state) {
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING -> {
                    val progress = it.progress.getInt(Const.Key.PROGRESS, 0)
                    if (progress == 0) {
                        setActionUiState(isActionRunning = true, isLoaderIndeterminate = false)
                    }
                    binding.loader.progress = progress
                }
                WorkInfo.State.SUCCEEDED -> {
                    setActionUiState(isActionRunning = false)
                    val data = it.outputData.getString(Const.Key.URL)
                    if(data.isNullOrEmpty()) {
                        CommonDialog.error(this, message = "Failed to upload the pdf")
                    } else {
                        binding.content.etCv.setText(data)
                        val message = "Pdf uploaded successfully. You MUST have to, " +
                                "Save Changes to save the new cv link."
                        CommonDialog.success(this, message = message)
                    }
                }
                WorkInfo.State.FAILED -> {
                    setActionUiState(isActionRunning = false)
                    val data = it.outputData.getString(Const.Key.MESSAGE)
                    val message = if(data.isNullOrEmpty())
                        getString(R.string.default_error_dialog_message) else data
                    CommonDialog.error(this, "Upload Failed", message)
                }
                else -> {
                    setActionUiState(isActionRunning = false)
                }
            }
        })
    }

    private fun setActivityResult(dataChanged: Boolean = true) {
        val intent = Intent()
        intent.putExtra(Const.Key.STUDENT, mStudent)
        intent.putExtra(Const.Key.UPDATED, dataChanged)
        setResult(Activity.RESULT_OK, intent)
    }
}
