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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
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
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.common.viewmodel.FileHandlerViewModel
import com.workfort.pstuian.app.ui.editprofile.EditProfileActivity
import com.workfort.pstuian.app.ui.faculty.adapter.PagerAdapter
import com.workfort.pstuian.app.ui.home.viewstate.SignInUserState
import com.workfort.pstuian.app.ui.signup.viewstate.SignOutState
import com.workfort.pstuian.app.ui.studentprofile.viewmodel.StudentProfileViewModel
import com.workfort.pstuian.app.ui.studentprofile.viewstate.ChangeProfileInfoState
import com.workfort.pstuian.app.ui.webview.WebViewActivity
import com.workfort.pstuian.databinding.ActivityStudentProfileBinding
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.PermissionUtil
import com.workfort.pstuian.util.helper.Toaster
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class StudentProfileActivity : BaseActivity<ActivityStudentProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityStudentProfileBinding
            = ActivityStudentProfileBinding::inflate

    private val mViewModel : StudentProfileViewModel by viewModel()
    private val mAuthViewModel : AuthViewModel by viewModel()
    private val mFileHandlerVM : FileHandlerViewModel by viewModel()

    override fun getToolbarId(): Int = R.id.toolbar

    override fun observeBroadcast() = Const.IntentAction.NOTIFICATION
    override fun onBroadcastReceived(intent: Intent) {
        handleNotificationIntent(intent)
    }

    private lateinit var mFaculty: FacultyEntity
    private lateinit var mBatch: BatchEntity
    private lateinit var mStudent: StudentEntity
    private var isSignedIn = false

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

        observeSignedInUser()
        observeProfileImageChange()
        observeNameChange()
        observeBioChange()
        observeSignOut()
        lifecycleScope.launch {
            mAuthViewModel.intent.send(AuthIntent.GetSignInUser)
        }
    }

    private fun setUiData() {
        title = mStudent.name
        with(binding) {
            if(mStudent.imageUrl.isNullOrEmpty()) {
                imgAvatar.setImageResource(R.drawable.img_placeholder_profile)
            } else {
                imgAvatar.load(mStudent.imageUrl) {
                    placeholder(R.drawable.img_placeholder_profile)
                    error(R.drawable.img_placeholder_profile)
                }
            }
            if(mStudent.bio.isNullOrEmpty()) {
                tvBio.visibility = View.GONE
            } else {
                tvBio.visibility = View.VISIBLE
                tvBio.text = mStudent.bio
            }

            btnCall.setOnClickListener { promptCall(mStudent.phone) }
            btnEmail.setOnClickListener { promptEmail(mStudent.email) }
            btnCamera.setOnClickListener { chooseImage() }
            btnEditBio.setOnClickListener { promptChangeBio(mStudent) }
            btnSignOut.setOnClickListener { promptSignOut() }
            btnEdit.setOnClickListener {
                val action = if(tabs.selectedTabPosition == 0) {
                    Const.Key.EDIT_ACADEMIC
                } else Const.Key.EDIT_CONNECT

                gotToEditProfile(mStudent, action)
            }
        }
    }

    private fun initTabs() {
        pagerAdapter = PagerAdapter(this)
        pagerAdapter.addItem(createAcademicFragment())
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
        binding.tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(binding.btnEdit.visibility == View.VISIBLE) {
                    binding.btnEdit.setText(when (tab.position) {
                        0 -> R.string.txt_edit_academic
                        1 -> R.string.txt_edit_connect
                        else -> R.string.txt_unknown
                    })
                    if(!binding.btnEdit.isExtended) {
                        binding.btnEdit.extend()
                        lifecycleScope.launch {
                            delay(1000)
                            binding.btnEdit.shrink()
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun createAcademicFragment() = ProfilePagerItemFragment.instance(getAcademicInfoList(),
        object : ProfileInfoClickEvent() {
            override fun onAction(item: ProfileInfoItem) {
                when (item.action) {
                    ProfileInfoAction.EDIT -> {
                        when(item.actionData) {
                            mStudent.name -> {promptChangeName(mStudent)}
                            else -> {}
                        }
                    }
                    else -> { }
                }
            }
        })

    private fun createConnectFragment() = ProfilePagerItemFragment.instance(getConnectInfoList(),
        object : ProfileInfoClickEvent() {
            override fun onAction(item: ProfileInfoItem) {
                when (item.action) {
                    ProfileInfoAction.CALL -> { promptCall(item.actionData) }
                    ProfileInfoAction.MAIL -> { promptEmail(item.actionData) }
                    ProfileInfoAction.DOWNLOAD -> { promptDownloadCv(item.actionData) }
                    ProfileInfoAction.OPEN_LINK -> item.actionData?.let { openLink(it) }
                    else -> { }
                }
            }
        })

    private fun getAcademicInfoList(): List<ProfileInfoItem> {
        val nameEntity = ProfileInfoItem(getString(R.string.txt_name), mStudent.name)
        ProfileInfoItem(getString(R.string.txt_name), mStudent.name,
            R.drawable.ic_pencil_circular_outline, ProfileInfoAction.EDIT, mStudent.name)
        if(isSignedIn) {
            nameEntity.actionIcon = R.drawable.ic_pencil_circular_outline
            nameEntity.action = ProfileInfoAction.EDIT
            nameEntity.actionData = mStudent.name
        }
        return listOf(
            nameEntity,
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
                R.drawable.ic_call, ProfileInfoAction.CALL, mStudent.phone
            ),
            ProfileInfoItem(
                getString(R.string.txt_email), mStudent.email ?: "~",
                R.drawable.ic_email, ProfileInfoAction.MAIL, mStudent.email
            ),
            ProfileInfoItem(
                getString(R.string.txt_cv), mStudent.cvLink ?: "~",
                R.drawable.ic_download, ProfileInfoAction.DOWNLOAD, mStudent.cvLink
            ),
            ProfileInfoItem(
                getString(R.string.txt_linked_in), mStudent.linkedIn ?: "~",
                R.drawable.ic_web, ProfileInfoAction.OPEN_LINK, mStudent.linkedIn
            ),
            ProfileInfoItem(
                getString(R.string.txt_facebook), mStudent.fbLink ?: "~",
                R.drawable.ic_web, ProfileInfoAction.OPEN_LINK, mStudent.fbLink
            ),
        )
    }

    private fun observeSignedInUser() {
        lifecycleScope.launch {
            mAuthViewModel.signInUserState.collect {
                when (it) {
                    is SignInUserState.Idle -> {
                        setSignInUiState(isSignedIn = false)
                    }
                    is SignInUserState.Loading -> { }
                    is SignInUserState.User -> {
                        isSignedIn = it.user.id == mStudent.id
                        setSignInUiState(isSignedIn)
                        initTabs()
                    }
                    is SignInUserState.Error -> {
                        isSignedIn = false
                        setSignInUiState(false)
                        Timber.e(it.error)
                    }
                }
            }
        }
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
        mFileHandlerVM.compressImage(this, uri, fileName).observe(this, {
            when (it.state) {
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING -> {
                    setActionUiState(isActionRunning = true)
                }
                WorkInfo.State.SUCCEEDED -> {
                    setActionUiState(isActionRunning = false)
                    val file = File(cacheDir, fileName)
                    if(file.exists()) {
                        uploadProfileImage(fileName)
                    } else {
                        CommonDialog.error(this, message = "Failed to resize the image")
                    }
                }
                WorkInfo.State.FAILED -> {
                    setActionUiState(isActionRunning = false)
                    CommonDialog.error(this, "Failed to resize the image\"")
                }
                else -> {
                    setActionUiState(isActionRunning = false)
                }
            }
        })
    }

    // The file should be exist in the cache directory
    private fun uploadProfileImage(fileName: String) {
        mFileHandlerVM.uploadImage(this, fileName).observe(this, {
            when (it.state) {
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING -> {
                    val progress = it.progress.getInt(Const.Key.PROGRESS, 0)
                    if (progress == 0) {
                        setActionUiState(isActionRunning = true, isLoaderIndeterminate = false)
                    }
                    binding.loaderLarger.progress = progress
                }
                WorkInfo.State.SUCCEEDED -> {
                    setActionUiState(isActionRunning = false)
                    val data = it.outputData.getString(Const.Key.URL)
                    if(data.isNullOrEmpty()) {
                        CommonDialog.error(this, message = "Failed to upload the image")
                    } else {
                        mViewModel.changeProfileImage(mStudent, data)
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

    private fun observeProfileImageChange() {
        lifecycleScope.launch {
            mViewModel.changeProfileImageState.collect {
                when (it) {
                    is ChangeProfileInfoState.Idle -> {
                    }
                    is ChangeProfileInfoState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is ChangeProfileInfoState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        CommonDialog.success(this@StudentProfileActivity)
                    }
                    is ChangeProfileInfoState.Error -> {
                        setActionUiState(isActionRunning = false)
                        setUiData()
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@StudentProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    private var changeNameDialog: AlertDialog? = null
    private fun promptChangeName(student: StudentEntity) {
        changeNameDialog = CommonDialog.changeName(this, student.name, { newName ->
            changeNameDialog?.dismiss()
            mViewModel.changeName(student, newName)
        })
    }

    private fun observeNameChange() {
        lifecycleScope.launch {
            mViewModel.changeNameState.collect {
                when (it) {
                    is ChangeProfileInfoState.Idle -> {
                    }
                    is ChangeProfileInfoState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is ChangeProfileInfoState.Success<*> -> {
                        setActivityResult()
                        mStudent.name = it.data as String
                        title = it.data
                        //notify name change in academic tab
                        initTabs()
                        setActionUiState(isActionRunning = false)
                        CommonDialog.success(this@StudentProfileActivity)
                    }
                    is ChangeProfileInfoState.Error -> {
                        setActionUiState(isActionRunning = false)
                        setUiData()
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@StudentProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    private var changeBioDialog: AlertDialog? = null
    private fun promptChangeBio(student: StudentEntity) {
        changeBioDialog = CommonDialog.changeBio(this, student.bio?: "", { newBio ->
            changeBioDialog?.dismiss()
            mViewModel.changeBio(student, newBio)
        })
    }

    private fun observeBioChange() {
        lifecycleScope.launch {
            mViewModel.changeBioState.collect {
                when (it) {
                    is ChangeProfileInfoState.Idle -> {
                    }
                    is ChangeProfileInfoState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is ChangeProfileInfoState.Success<*> -> {
                        setActivityResult()
                        mStudent.bio = it.data as String
                        binding.tvBio.text = it.data
                        setActionUiState(isActionRunning = false)
                        CommonDialog.success(this@StudentProfileActivity)
                    }
                    is ChangeProfileInfoState.Error -> {
                        setActionUiState(isActionRunning = false)
                        setUiData()
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@StudentProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    private fun setActionUiState(isActionRunning: Boolean, isLoaderIndeterminate: Boolean = true) {
        with(binding) {
            loaderLarger.visibility = View.GONE
            if(isActionRunning) {
                loaderLarger.isIndeterminate = isLoaderIndeterminate
                loaderLarger.visibility = View.VISIBLE
            }
        }
    }

    private fun promptSignOut() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.txt_sign_out)
            .setMessage("Are you surely want to sign out?")
            .setPositiveButton(R.string.txt_sign_out) { _,_ ->
                lifecycleScope.launch {
                    mAuthViewModel.intent.send(AuthIntent.SignOut)
                }
            }
            .setNegativeButton(R.string.txt_dismiss) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun observeSignOut() {
        lifecycleScope.launch {
            mAuthViewModel.signOutState.collect {
                when (it) {
                    is SignOutState.Idle -> {
                    }
                    is SignOutState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is SignOutState.Success -> {
                        setActionUiState(isActionRunning = false)
                        isSignedIn = false
                        setSignInUiState(isSignedIn = false)
                        initTabs()
                        Toaster.show(it.message)
                    }
                    is SignOutState.Error -> {
                        setActionUiState(isActionRunning = false)
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@StudentProfileActivity, message = msg)
                    }
                }
            }
        }
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

    private fun promptDownloadCv(link: String?) {
        if(link.isNullOrEmpty()) {
            Toaster.show(R.string.txt_error_download_cv)
            return
        }
        val msg = "${getString(R.string.txt_msg_download_cv)} $link"
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.txt_title_download_cv)
            .setMessage(msg)
            .setPositiveButton(R.string.txt_download) { _,_ ->
                requestFileCreatePermission()
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

    //according to documentation this should work. But unfortunately nope :D
//    private val chooseImageResultLauncher = registerForActivityResult(GetContent()) { uri: Uri? ->
//        if (uri != null) {
//            Timber.e("Data $uri")
//        } else {
//            Toaster.show("Failed to choose image")
//        }
//    }

    private fun setSignInUiState(isSignedIn: Boolean) {
        val signInVisibility = if(isSignedIn) View.VISIBLE else View.GONE
        val signOutVisibility = if(isSignedIn) View.GONE else View.VISIBLE
        with(binding) {
            btnCamera.visibility = signInVisibility
            btnEditBio.visibility = signInVisibility
            btnSignOut.visibility = signInVisibility
            btnEdit.visibility = signInVisibility
            btnCall.visibility = signOutVisibility
            btnEmail.visibility = signOutVisibility
            if(isSignedIn) {
                if(mStudent.bio.isNullOrEmpty()) {
                    tvBio.visibility = View.VISIBLE
                    tvBio.text = getString(R.string.hint_add_bio)
                }
                lifecycleScope.launch {
                    delay(1000)
                    btnEdit.shrink()
                }
            }
        }
    }

    private fun gotToEditProfile(student: StudentEntity, action: String) {
        val intent = Intent(this, EditProfileActivity::class.java)
            .apply {
                putExtra(Const.Key.STUDENT, student)
                putExtra(Const.Key.EDIT_ACTION, action)
            }
        startActivityForResult.launch(intent)
    }

    private val startActivityForResult = registerForActivityResult(
        StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            result?.data?.getParcelableExtra<StudentEntity>(Const.Key.STUDENT)?.let {
                mStudent = it
            }
            setUiData()
            initTabs()
            setSignInUiState(isSignedIn)
            setActivityResult()
        }
    }

    private fun setActivityResult(dataChanged: Boolean = true) {
        val intent = Intent()
        intent.putExtra(Const.Key.STUDENT, mStudent)
        intent.putExtra(Const.Key.UPDATED, dataChanged)
        setResult(Activity.RESULT_OK, intent)
    }
}
