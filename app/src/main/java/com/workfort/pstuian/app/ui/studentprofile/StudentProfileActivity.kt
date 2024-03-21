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
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoAction
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoClickEvent
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoItem
import com.workfort.pstuian.app.ui.common.blooddonationlist.BloodDonationListDialogFragment
import com.workfort.pstuian.app.ui.common.checkinlist.CheckInListDialogFragment
import com.workfort.pstuian.app.ui.common.devicelist.DeviceListDialogFragment
import com.workfort.pstuian.app.ui.common.dialog.CommonDialog
import com.workfort.pstuian.app.ui.common.fragment.ProfilePagerItemFragment
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.editprofile.EditStudentProfileActivity
import com.workfort.pstuian.app.ui.faculty.adapter.PagerAdapter
import com.workfort.pstuian.app.ui.home.HomeActivity
import com.workfort.pstuian.app.ui.imagepreview.ImagePreviewActivity
import com.workfort.pstuian.app.ui.studentprofile.intent.StudentProfileIntent
import com.workfort.pstuian.app.ui.studentprofile.viewmodel.StudentProfileViewModel
import com.workfort.pstuian.app.ui.webview.WebViewActivity
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.databinding.ActivityStudentProfileBinding
import com.workfort.pstuian.model.ProgressRequestState
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.StudentProfile
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.PermissionUtil
import com.workfort.pstuian.util.helper.Toaster
import com.workfort.pstuian.workmanager.FileHandlerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    private lateinit var profile: StudentProfile
    private lateinit var pagerAdapter: PagerAdapter
    private val mLinkUtil by lazy { LinkUtil(this) }

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        val studentId = intent.getIntExtra(Const.Key.STUDENT_ID, -1)
        if(studentId == -1) {
            finish()
            return
        }
        loadProfile(studentId)
        observeProfile()
        observeProfileImageChange()
        observeNameChange()
        observeBioChange()
        observeSignOut()
        observePasswordChange()
        observeAccountDelete()
    }

    private fun loadProfile(studentId: Int) {
        lifecycleScope.launch {
            mViewModel.intent.send(StudentProfileIntent.GetProfile(studentId))
        }
    }

    /**
     * If user profile is loaded successfully, set ui data
     * Else show blocking error dialog
     * */
    private fun observeProfile() {
        lifecycleScope.launch {
            mViewModel.getProfileState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> {
                        binding.loaderOverlay.visibility = View.VISIBLE
                        binding.loader.visibility = View.VISIBLE
                        binding.loader.isIndeterminate = true
                    }
                    is RequestState.Success<*> -> {
                        binding.loaderOverlay.visibility = View.GONE
                        binding.loader.visibility = View.GONE
                        profile = it.data as StudentProfile
                        setUiData()
                        initTabs()
                    }
                    is RequestState.Error -> {
                        binding.loaderOverlay.visibility = View.GONE
                        binding.loader.visibility = View.GONE
                        val msg = it.error?: "Failed to load data"
                        CommonDialog.error(
                            this@StudentProfileActivity,
                            message = msg,
                            cancelable = false
                        ) { finish() }
                    }
                }
            }
        }
    }

    private fun setUiData() {
        val student = profile.student
        setSignInUiState(isSignedIn = profile.isSignedIn)
        title = student.name
        with(binding.header) {
            btnCall.setOnClickListener { promptCall(student.phone) }
            btnEmail.setOnClickListener { promptEmail(student.email) }
            btnSignOut.setOnClickListener { promptSignOut() }
        }
        with(binding.content) {
            if(student.imageUrl.isNullOrEmpty()) {
                imgAvatar.setImageResource(R.drawable.img_placeholder_profile)
            } else {
                imgAvatar.load(student.imageUrl?: "") {
                    placeholder(R.drawable.img_placeholder_profile)
                    error(R.drawable.img_placeholder_profile)
                }

                imgAvatar.setOnClickListener {
                    val intent = Intent(this@StudentProfileActivity,
                        ImagePreviewActivity::class.java)
                    intent.putExtra(Const.Key.URL, student.imageUrl)
                    intent.putExtra(
                        Const.Key.EXTRA_IMAGE_TRANSITION_NAME,
                        imgAvatar.transitionName)
                    startActivity(intent)
                }
            }
            tvBio.text = if(student.bio.isNullOrEmpty()) getString(R.string.hint_add_bio) else
                student.bio
            tvBio.visibility = if(student.bio.isNullOrEmpty()) View.GONE else View.VISIBLE
            if(profile.isSignedIn) tvBio.visibility = View.VISIBLE

            btnCamera.setOnClickListener { chooseImage() }
            btnEditBio.setOnClickListener { promptChangeBio(student) }
            btnEdit.setOnClickListener {
                val action = if(tabs.selectedTabPosition == 0) Const.Key.EDIT_ACADEMIC
                else Const.Key.EDIT_CONNECT
                gotToEditProfile(student, action)
            }
        }
    }

    private fun initTabs() {
        pagerAdapter = PagerAdapter(this)
        pagerAdapter.addItem(createAcademicFragment())
        pagerAdapter.addItem(createConnectFragment())
        if(profile.isSignedIn) pagerAdapter.addItem(createOptionFragment())
        binding.content.viewPager.adapter = pagerAdapter
        binding.content.viewPager.offscreenPageLimit = pagerAdapter.itemCount

        TabLayoutMediator(binding.content.tabs, binding.content.viewPager) { tab, position ->
            tab.text = getString(
                when (position) {
                    0 -> R.string.txt_academic
                    1 -> R.string.txt_connect
                    2 -> R.string.txt_option
                    else -> R.string.txt_unknown
                }
            )
        }.attach()
        binding.content.tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(!profile.isSignedIn) return
                binding.content.btnEdit.visibility = if(tab.position == 2)
                    View.GONE else View.VISIBLE
                if(binding.content.btnEdit.visibility == View.VISIBLE) {
                    binding.content.btnEdit.setText(when (tab.position) {
                        0 -> R.string.txt_edit_academic
                        1 -> R.string.txt_edit_connect
                        else -> R.string.txt_unknown
                    })
                    if(!binding.content.btnEdit.isExtended) {
                        binding.content.btnEdit.extend()
                        lifecycleScope.launch {
                            delay(1000)
                            binding.content.btnEdit.shrink()
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
                    ProfileInfoAction.Edit -> {
                        when(item.actionData) {
                            profile.student.name -> {promptChangeName(profile.student)}
                            else -> Unit
                        }
                    }
                    else -> Unit
                }
            }
        })

    private fun createConnectFragment() = ProfilePagerItemFragment.instance(getConnectInfoList(),
        object : ProfileInfoClickEvent() {
            override fun onAction(item: ProfileInfoItem) {
                when (item.action) {
                    ProfileInfoAction.Call -> { promptCall(item.actionData) }
                    ProfileInfoAction.Mail -> { promptEmail(item.actionData) }
                    ProfileInfoAction.Download -> { promptDownloadCv(item.actionData) }
                    ProfileInfoAction.Link -> openLink(item.actionData)
                    else -> { }
                }
            }
        })

    private fun createOptionFragment() = ProfilePagerItemFragment.instance(getOptionList(),
    object : ProfileInfoClickEvent() {
        override fun onAction(item: ProfileInfoItem) {
            when (item.action) {
                ProfileInfoAction.Password -> { promptChangePassword() }
                ProfileInfoAction.BloodDonationList -> { promptBloodDonationList() }
                ProfileInfoAction.CheckInList -> { promptCheckInList() }
                ProfileInfoAction.SignedInDevices -> { promptSignedInDevices() }
                ProfileInfoAction.DeleteAccount -> { promptDeleteAccount() }
                else -> Unit
            }
        }
    })

    private fun getAcademicInfoList(): List<ProfileInfoItem> {
        val student = profile.student
        val faculty = profile.faculty
        val batch = profile.batch
        val nameEntity = ProfileInfoItem(getString(R.string.txt_name), student.name)
        if(profile.isSignedIn) {
            nameEntity.actionIcon = R.drawable.ic_pencil_circular_outline
            nameEntity.action = ProfileInfoAction.Edit
            nameEntity.actionData = student.name
        }
        return listOf(
            nameEntity,
            ProfileInfoItem(getString(R.string.txt_id), student.id.toString()),
            ProfileInfoItem(getString(R.string.txt_registration_number), student.reg),
            ProfileInfoItem(getString(R.string.txt_blood_group), student.blood ?: "~"),
            ProfileInfoItem(getString(R.string.txt_faculty), faculty.title),
            ProfileInfoItem(getString(R.string.txt_session), student.session),
            ProfileInfoItem(getString(R.string.txt_batch), batch.name),
        )
    }

    private fun getConnectInfoList(): List<ProfileInfoItem> {
        val student = profile.student
        return listOf(
            ProfileInfoItem(getString(R.string.txt_address), student.address ?: "~"),
            ProfileInfoItem(
                getString(R.string.txt_phone), student.phone ?: "~",
                R.drawable.ic_call, ProfileInfoAction.Call, student.phone
            ),
            ProfileInfoItem(
                getString(R.string.txt_email), student.email ?: "~",
                R.drawable.ic_email, ProfileInfoAction.Mail, student.email
            ),
            ProfileInfoItem(
                getString(R.string.txt_cv), student.cvLink ?: "~",
                R.drawable.ic_download, ProfileInfoAction.Download, student.cvLink
            ),
            ProfileInfoItem(
                getString(R.string.txt_linked_in), student.linkedIn ?: "~",
                R.drawable.ic_web, ProfileInfoAction.Link, student.linkedIn
            ),
            ProfileInfoItem(
                getString(R.string.txt_facebook), student.fbLink ?: "~",
                R.drawable.ic_web, ProfileInfoAction.Link, student.fbLink
            ),
        )
    }

    private fun getOptionList(): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem(
                getString(R.string.txt_password), getString(R.string.txt_change_password),
                R.drawable.ic_pencil_circular_outline, ProfileInfoAction.Password, "~"
            ),
            ProfileInfoItem(
                getString(R.string.txt_blood_donation), getString(R.string.txt_my_donation_list),
                R.drawable.ic_pencil_circular_outline, ProfileInfoAction.BloodDonationList, "~"
            ),
            ProfileInfoItem(
                getString(R.string.txt_check_in), getString(R.string.txt_my_check_in_list),
                R.drawable.ic_pencil_circular_outline, ProfileInfoAction.CheckInList, "~"
            ),
            ProfileInfoItem(
                getString(R.string.txt_devices), getString(R.string.txt_signed_in_devices),
                R.drawable.ic_pencil_circular_outline, ProfileInfoAction.SignedInDevices, "~"
            ),
            ProfileInfoItem(
                getString(R.string.txt_account), getString(R.string.txt_delete_account),
                R.drawable.ic_delete_outline_red, ProfileInfoAction.DeleteAccount, "~"
            ),
        )
    }

    private fun requestFileCreatePermission() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "cv_${profile.student.id}.pdf")
        }

        fileCreateResultLauncher.launch(intent)
    }

    private val fileCreateResultLauncher = registerForActivityResult(StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->
                profile.student.cvLink?.let { downloadFile(it, uri.toString()) }
            }
        } else {
            Toaster.show("Failed to save the file")
        }
    }

    private fun downloadFile(url: String, storageUri: String) {
        mFileHandlerVM.download(this, url, storageUri)
        lifecycleScope.launch {
            mFileHandlerVM.downloadFileState.collect {
                when (it) {
                    is ProgressRequestState.Idle -> Unit
                    is ProgressRequestState.Loading -> {
                        if (it.progress == 0) {
                            binding.loaderOverlay.visibility = View.VISIBLE
                            binding.loader.visibility = View.VISIBLE
                            binding.loader.isIndeterminate = false
                            binding.labelDownload.visibility = View.VISIBLE
                        }
                        binding.loader.progress = it.progress
                    }
                    is ProgressRequestState.Success<*> -> {
                        binding.loaderOverlay.visibility = View.GONE
                        binding.loader.visibility = View.GONE
                        binding.labelDownload.visibility = View.GONE
                        CommonDialog.success(this@StudentProfileActivity, it.data as String)
                    }
                    is ProgressRequestState.Error -> {
                        binding.loaderOverlay.visibility = View.GONE
                        binding.loader.visibility = View.GONE
                        binding.labelDownload.visibility = View.GONE
                        val msg = it.error ?: "Download failed"
                        CommonDialog.error(this@StudentProfileActivity, msg)
                    }
                }
            }
        }
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

    private val permissionResultLauncher = registerForActivityResult(RequestPermission()) {
            isGranted ->
        if(isGranted) {
            chooseImage()
        } else {
            Toaster.show("Storage permission is necessary")
        }
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

    private var proPicChangeDialog: AlertDialog? = null
    private fun showChangeProPicDialog(imgUri: Uri) {
        proPicChangeDialog = CommonDialog.changeProPic(this, imgUri, {
            chooseImage()
        }, {
            binding.content.imgAvatar.load(imgUri.toString())
            proPicChangeDialog?.dismiss()
            compressAndUploadProfileImage(imgUri)
        })
    }

    // the compressed file should be stored in the cache folder
    private fun compressAndUploadProfileImage(uri: Uri) {
        val fileName = "${profile.student.id}.jpeg"
        mFileHandlerVM.compressAndUploadImage(
            this,
            uri,
            fileName,
            cacheDir,
            NetworkConst.Params.UserType.STUDENT,
        )
        lifecycleScope.launch {
            mFileHandlerVM.compressAndUploadImageState.collect {
                when (it) {
                    is ProgressRequestState.Idle -> Unit
                    is ProgressRequestState.Loading -> {
                        if (it.progress == 0) {
                            setActionUiState(isActionRunning = true, isLoaderIndeterminate = false)
                        }
                        binding.content.loaderLarger.progress = it.progress
                    }
                    is ProgressRequestState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        lifecycleScope.launch {
                            mViewModel.intent.send(
                                StudentProfileIntent.ChangeProfileImage(profile.student, it.data as String,)
                            )
                        }
                    }
                    is ProgressRequestState.Error -> {
                        setActionUiState(isActionRunning = false)
                        val msg = it.error ?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@StudentProfileActivity, "Upload Failed", msg)
                    }
                }
            }
        }
    }

    private fun observeProfileImageChange() {
        lifecycleScope.launch {
            mViewModel.changeProfileImageState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is RequestState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        CommonDialog.success(this@StudentProfileActivity)
                    }
                    is RequestState.Error -> {
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
            lifecycleScope.launch {
                mViewModel.intent.send(StudentProfileIntent.ChangeName(student, newName))
            }
        })
    }

    private fun observeNameChange() {
        lifecycleScope.launch {
            mViewModel.changeNameState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is RequestState.Success<*> -> {
                        setActivityResult()
                        profile.student.name = it.data as String
                        title = it.data as String
                        //notify name change in academic tab
                        initTabs()
                        setActionUiState(isActionRunning = false)
                    }
                    is RequestState.Error -> {
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
            lifecycleScope.launch {
                mViewModel.intent.send(StudentProfileIntent.ChangeBio(student, newBio))
            }
        })
    }

    private fun observeBioChange() {
        lifecycleScope.launch {
            mViewModel.changeBioState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is RequestState.Success<*> -> {
                        setActivityResult()
                        profile.student.bio = it.data as String
                        binding.content.tvBio.text = it.data as String
                        binding.content.tvBio.visibility = View.VISIBLE
                        setActionUiState(isActionRunning = false)
                    }
                    is RequestState.Error -> {
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
        with(binding.content) {
            loaderLarger.visibility = View.GONE
            if(isActionRunning) {
                loaderLarger.isIndeterminate = isLoaderIndeterminate
                loaderLarger.visibility = View.VISIBLE
            }
        }
    }

    private fun promptSignOut() {
        CommonDialog.confirmation(
            this,
            getString(R.string.txt_sign_out),
            getString(R.string.msg_sign_out),
            getString(R.string.txt_sign_out),
        ) {
            signOut()
        }
    }

    private fun signOut(fromAllDevices: Boolean = false) {
        lifecycleScope.launch {
            mAuthViewModel.intent.send(AuthIntent.SignOut(fromAllDevices))
        }
    }

    private fun observeSignOut() {
        lifecycleScope.launch {
            mAuthViewModel.signOutState.collect {
                when (it) {
                    is RequestState.Idle -> {
                    }
                    is RequestState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is RequestState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        profile.isSignedIn = false
//                        setSignInUiState(isSignedIn = false)
//                        initTabs()
                        Toaster.show(it.data as String)
                        launchActivity<HomeActivity>()
                        finish()
                    }
                    is RequestState.Error -> {
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

    private fun openLink(link: String?) {
        if(link.isNullOrEmpty()) {
            Toaster.show("Invalid Link")
            return
        }
        if(link.contains("linkedin.com") || link.contains("facebook.com")) {
            mLinkUtil.openBrowser(link)
        } else {
            launchActivity<WebViewActivity>(Pair(Const.Key.URL, link))
        }
    }

    private fun promptChangePassword() {
        CommonDialog.changePassword(this, { oldPassword, newPassword ->
            lifecycleScope.launch {
                mAuthViewModel.intent.send(AuthIntent.ChangePassword(oldPassword, newPassword))
            }
        })
    }

    private fun observePasswordChange() {
        lifecycleScope.launch {
            mAuthViewModel.changePasswordState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUiState(isActionRunning = true)
                    is RequestState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        CommonDialog.success(
                            this@StudentProfileActivity,
                            message = it.data as String,
                        )
                    }
                    is RequestState.Error -> {
                        setActionUiState(isActionRunning = false)
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@StudentProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    private fun promptBloodDonationList() {
        BloodDonationListDialogFragment.show(
            supportFragmentManager,
            profile.student.id,
            NetworkConst.Params.UserType.STUDENT,
            profile.isSignedIn
        )
    }

    private fun promptCheckInList() {
        CheckInListDialogFragment.show(
            supportFragmentManager,
            profile.student.id,
            NetworkConst.Params.UserType.STUDENT,
            profile.isSignedIn
        )
    }

    private fun promptSignedInDevices() {
        DeviceListDialogFragment.show(supportFragmentManager) {
            signOut(true)
        }
    }

    private fun promptDeleteAccount() {
        CommonDialog.deleteAccount(this) { password ->
            lifecycleScope.launch {
                mAuthViewModel.intent.send(AuthIntent.DeleteAccount(password))
            }
        }
    }

    private fun observeAccountDelete() {
        lifecycleScope.launch {
            mAuthViewModel.accountDeleteState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is RequestState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        profile.isSignedIn = false
                        Toaster.show("Account deleted successfully!")
                        launchActivity<HomeActivity>()
                        finish()
                    }
                    is RequestState.Error -> {
                        setActionUiState(isActionRunning = false)
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

    private fun setSignInUiState(isSignedIn: Boolean) {
        val signInVisibility = if(isSignedIn) View.VISIBLE else View.GONE
        val signOutVisibility = if(isSignedIn) View.GONE else View.VISIBLE
        with(binding.header) {
            btnSignOut.visibility = signInVisibility
            btnCall.visibility = signOutVisibility
            btnEmail.visibility = signOutVisibility
        }
        with(binding.content) {
            btnCamera.visibility = signInVisibility
            btnEditBio.visibility = signInVisibility
            btnEdit.visibility = signInVisibility
            if(isSignedIn) {
                lifecycleScope.launch {
                    delay(1000)
                    btnEdit.shrink()
                }
            }
        }
    }

    private fun gotToEditProfile(student: StudentEntity, action: String) {
        val intent = Intent(this, EditStudentProfileActivity::class.java)
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
                loadProfile(it.id)
            }
            setActivityResult()
        }
    }

    private fun setActivityResult(dataChanged: Boolean = true) {
        val intent = Intent()
        intent.putExtra(Const.Key.STUDENT, profile.student)
        intent.putExtra(Const.Key.UPDATED, dataChanged)
        setResult(Activity.RESULT_OK, intent)
    }
}
