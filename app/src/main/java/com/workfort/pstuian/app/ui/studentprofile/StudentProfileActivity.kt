package com.workfort.pstuian.app.ui.studentprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoAction
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoClickEvent
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoItem
import com.workfort.pstuian.app.ui.common.fragment.ProfilePagerItemFragment
import com.workfort.pstuian.app.ui.common.viewmodel.FileHandlerViewModel
import com.workfort.pstuian.app.ui.faculty.adapter.PagerAdapter
import com.workfort.pstuian.app.ui.home.viewstate.SignInUserState
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.studentprofile.viewmodel.StudentProfileViewModel
import com.workfort.pstuian.app.ui.studentprofile.viewstate.ChangeProfileImageState
import com.workfort.pstuian.databinding.ActivityStudentProfileBinding
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.PermissionUtil
import com.workfort.pstuian.util.helper.Toaster
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class StudentProfileActivity : BaseActivity<ActivityStudentProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityStudentProfileBinding
            = ActivityStudentProfileBinding::inflate

    private val mViewModel : StudentProfileViewModel by viewModel()
    private val mAuthVM : AuthViewModel by viewModel()
    private val mFileHandlerVM : FileHandlerViewModel by viewModel()

    override fun getToolbarId(): Int = R.id.toolbar

    private lateinit var mFaculty: FacultyEntity
    private lateinit var mBatch: BatchEntity
    private lateinit var mStudent: StudentEntity

    private lateinit var pagerAdapter: PagerAdapter

    private val mLinkUtil by lazy { LinkUtil(this) }

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        val faculty = intent.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)
        if(faculty == null) finish()
        else mFaculty = faculty

        val batch = intent.getParcelableExtra<BatchEntity>(Const.Key.BATCH)
        if(batch == null) finish()
        else mBatch = batch

        val student = intent.getParcelableExtra<StudentEntity>(Const.Key.STUDENT)
        if(student == null) finish()
        else mStudent = student

        setUiData()
        initTabs()

        binding.btnCall.setOnClickListener {
            mStudent.phone?.let {
                mLinkUtil.callTo(it)
            }
        }

        binding.btnEmail.setOnClickListener {
            mStudent.email?.let {
                mLinkUtil.sendEmail(it)
            }
        }

        binding.btnCamera.setOnClickListener {
            chooseImage()
        }

        observeSignedInUser()
        observeProfileImageChange()
        lifecycleScope.launch {
            mAuthVM.intent.send(AuthIntent.GetSignInUser)
        }
    }

    private fun setUiData() {
        title = ""
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
        }
    }

    private fun initTabs() {
        pagerAdapter = PagerAdapter(this)
        pagerAdapter.addItem(ProfilePagerItemFragment.instance(getAcademicInfoList()))
        pagerAdapter.addItem(createConnectFragment())
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = pagerAdapter.itemCount

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(
                when (position) {
                    0 -> R.string.txt_academic
                    1 -> R.string.txt_connect
                    else -> R.string.txt_unknown
                }
            )
        }.attach()
    }

    private fun observeSignedInUser() {
        lifecycleScope.launch {
            mAuthVM.signInUserState.collect {
                when (it) {
                    is SignInUserState.Idle -> {
                        binding.btnCamera.visibility = View.GONE
                    }
                    is SignInUserState.Loading -> {
                        binding.btnCamera.visibility = View.GONE
                    }
                    is SignInUserState.User -> {
                        if(it.user.id == mStudent.id) {
                            binding.btnCamera.visibility = View.VISIBLE
                            binding.btnCall.visibility = View.GONE
                            binding.btnEmail.visibility = View.GONE
                        }
                    }
                    is SignInUserState.Error -> {
                        binding.btnCamera.visibility = View.GONE
                        Timber.e(it.error)
                    }
                }
            }
        }
    }

    private fun createConnectFragment() = ProfilePagerItemFragment.instance(getConnectInfoList(),
        object : ProfileInfoClickEvent() {
            override fun onAction(item: ProfileInfoItem) {
                when (item.action) {
                    ProfileInfoAction.CALL -> {
                        mLinkUtil.callTo(item.actionData)
                    }
                    ProfileInfoAction.MAIL -> {
                        mLinkUtil.sendEmail(item.actionData)
                    }
                    ProfileInfoAction.DOWNLOAD -> {
                        requestFileCreatePermission()
                    }
                    ProfileInfoAction.OPEN_LINK -> {
                        mLinkUtil.openBrowser(item.actionData)
                    }
                    else -> {
                    }
                }
            }
        })

    private fun getAcademicInfoList(): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem(getString(R.string.txt_id), mStudent.id.toString()),
            ProfileInfoItem(getString(R.string.txt_registration_number), mStudent.reg),
            ProfileInfoItem(getString(R.string.txt_blood_group), mStudent.blood ?: "~"),
            ProfileInfoItem(getString(R.string.txt_faculty), mFaculty.title),
            ProfileInfoItem(getString(R.string.txt_session), mStudent.session),
            ProfileInfoItem(getString(R.string.txt_batch), mBatch.name),
        )
    }

    private fun getConnectInfoList(): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem(getString(R.string.txt_address), mStudent.address ?: "~"),
            ProfileInfoItem(
                getString(R.string.txt_phone), mStudent.phone ?: "~",
                R.drawable.ic_call, ProfileInfoAction.CALL, mStudent.phone ?: "~"
            ),
            ProfileInfoItem(
                getString(R.string.txt_email), mStudent.email ?: "~",
                R.drawable.ic_email, ProfileInfoAction.MAIL, mStudent.email ?: "~"
            ),
            ProfileInfoItem(
                getString(R.string.txt_cv), mStudent.cvLink ?: "~",
                R.drawable.ic_download, ProfileInfoAction.DOWNLOAD, mStudent.cvLink ?: "~"
            ),
            ProfileInfoItem(
                getString(R.string.txt_linked_in), mStudent.linkedIn ?: "~",
                R.drawable.ic_web, ProfileInfoAction.OPEN_LINK, mStudent.linkedIn ?: "~"
            ),
            ProfileInfoItem(
                getString(R.string.txt_facebook), mStudent.fbLink ?: "~",
                R.drawable.ic_web, ProfileInfoAction.OPEN_LINK, mStudent.fbLink ?: "~"
            ),
        )
    }

    private fun requestFileCreatePermission() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "cv_${mStudent.id}.pdf")
        }

        fileCreateResultLauncher.launch(intent)
    }

    private val fileCreateResultLauncher = registerForActivityResult(StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->
                mStudent.cvLink?.let { downloadFile(it, uri.toString()) }
            }
        } else {
            Toaster.show("Failed to save the file")
        }
    }

    private fun downloadFile(url: String, storageUri: String) {
        mFileHandlerVM.download(this, url, storageUri).observe(this, {
            when (it.state) {
                WorkInfo.State.RUNNING -> {
                    val progress = it.progress.getInt(Const.Key.PROGRESS, 0)
                    if (progress == 0) {
                        binding.loaderOverlay.visibility = View.VISIBLE
                        binding.loader.visibility = View.VISIBLE
                        binding.labelDownload.visibility = View.VISIBLE
                    }
                    binding.loader.progress = progress
                }
                WorkInfo.State.SUCCEEDED -> {
                    binding.loaderOverlay.visibility = View.GONE
                    binding.loader.visibility = View.GONE
                    binding.labelDownload.visibility = View.GONE
                    CommonDialog.success(this, "Download Successfully")
                }
                WorkInfo.State.FAILED -> {
                    binding.loaderOverlay.visibility = View.GONE
                    binding.loader.visibility = View.GONE
                    binding.labelDownload.visibility = View.GONE
                    CommonDialog.error(this, "Download failed")
                }
                else -> {
                    binding.loaderOverlay.visibility = View.GONE
                    binding.loader.visibility = View.GONE
                    binding.labelDownload.visibility = View.GONE
                }
            }
        })
    }

    @Suppress("DEPRECATION")
    private fun chooseImage() {
        if(!PermissionUtil.isAllowed(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            return
        }
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .apply {
            type = "image/jpeg"
        }
        startActivityForResult(pickIntent, Const.RequestCode.PICK_IMAGE)
//        chooseImageResultLauncher.launch("image/jpeg")
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Const.RequestCode.PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                proPicChangeDialog.let {  dialog ->
                    if(dialog != null && dialog.isShowing) dialog.dismiss()
                    showChangeProPicDialog(uri)
                }
            }
        }
    }

    private val permissionResultLauncher = registerForActivityResult(RequestPermission()) {
            isGranted ->
        if(isGranted) {
            chooseImage()
        } else {
            Toaster.show("Storage permission is necessary")
        }
    }

    private var proPicChangeDialog: AlertDialog? = null
    private fun showChangeProPicDialog(imgUri: Uri) {
        proPicChangeDialog = CommonDialog.changeProPic(this, imgUri, {
            chooseImage()
        }, {
            binding.imgAvatar.load(imgUri.toString())
            proPicChangeDialog?.dismiss()
            compressProfileImage(imgUri)
        })
    }

    private fun compressProfileImage(uri: Uri) {
        val fileName = "${mStudent.id}.jpeg"
        mFileHandlerVM.compress(this, uri, fileName).observe(this, {
            when (it.state) {
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING -> {
                    binding.btnCamera.visibility = View.GONE
                    binding.loaderProPicUpload.isIndeterminate = true
                    binding.loaderProPicUpload.visibility = View.VISIBLE
                }
                WorkInfo.State.SUCCEEDED -> {
                    binding.btnCamera.visibility = View.VISIBLE
                    binding.loaderProPicUpload.visibility = View.GONE
                    val file = File(cacheDir, fileName)
                    if(file.exists()) {
                        uploadProfileImage(fileName)
                    } else {
                        CommonDialog.error(this, message = "Failed to resize the image")
                    }
                }
                WorkInfo.State.FAILED -> {
                    binding.btnCamera.visibility = View.VISIBLE
                    binding.loaderProPicUpload.visibility = View.GONE
                    CommonDialog.error(this, "Failed to resize the image\"")
                }
                else -> {
                    binding.btnCamera.visibility = View.VISIBLE
                    binding.loaderProPicUpload.visibility = View.GONE
                }
            }
        })
    }

    // The file should be exist in the cache directory
    private fun uploadProfileImage(fileName: String) {
        mFileHandlerVM.upload(this, fileName).observe(this, {
            when (it.state) {
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING -> {
                    val progress = it.progress.getInt(Const.Key.PROGRESS, 0)
                    if (progress == 0) {
                        binding.btnCamera.visibility = View.GONE
                        binding.loaderProPicUpload.isIndeterminate = false
                        binding.loaderProPicUpload.visibility = View.VISIBLE
                    }
                    binding.loaderProPicUpload.progress = progress
                }
                WorkInfo.State.SUCCEEDED -> {
                    binding.btnCamera.visibility = View.GONE
                    binding.loaderProPicUpload.visibility = View.GONE
                    val data = it.outputData.getString(Const.Key.URL)
                    if(data.isNullOrEmpty()) {
                        CommonDialog.error(this, message = "Failed to upload the image")
                    } else {
                        mViewModel.changeProfileImage(mStudent.id, data)
                    }
                }
                WorkInfo.State.FAILED -> {
                    binding.btnCamera.visibility = View.VISIBLE
                    binding.loaderProPicUpload.visibility = View.GONE
                    CommonDialog.error(this, "Failed to upload the image")
                }
                else -> {
                    binding.btnCamera.visibility = View.VISIBLE
                    binding.loaderProPicUpload.visibility = View.GONE
                }
            }
        })
    }

    private fun observeProfileImageChange() {
        lifecycleScope.launch {
            mViewModel.changeProfileImageState.collect {
                when (it) {
                    is ChangeProfileImageState.Idle -> {
                    }
                    is ChangeProfileImageState.Loading -> {
                        binding.btnCamera.visibility = View.GONE
                        binding.loaderProPicUpload.isIndeterminate = true
                        binding.loaderProPicUpload.visibility = View.VISIBLE
                    }
                    is ChangeProfileImageState.Success -> {
                        binding.btnCamera.visibility = View.VISIBLE
                        binding.loaderProPicUpload.visibility = View.GONE
                        CommonDialog.success(this@StudentProfileActivity)
                    }
                    is ChangeProfileImageState.Error -> {
                        binding.btnCamera.visibility = View.VISIBLE
                        binding.loaderProPicUpload.visibility = View.GONE
                        setUiData()
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@StudentProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    //according to documentation this should work. But unfortunately nope :D
//    private val chooseImageResultLauncher = registerForActivityResult(GetContent()) { uri: Uri? ->
//        if (uri != null) {
//            Timber.e("Data $uri")
//        } else {
//            Toaster.show("Failed to choose image")
//        }
//    }
}
