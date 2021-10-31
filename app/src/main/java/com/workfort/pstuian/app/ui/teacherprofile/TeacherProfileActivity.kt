package com.workfort.pstuian.app.ui.teacherprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoAction
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoClickEvent
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoItem
import com.workfort.pstuian.app.ui.common.fragment.ProfilePagerItemFragment
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.common.viewmodel.FileHandlerViewModel
import com.workfort.pstuian.app.ui.editprofile.EditTeacherProfileActivity
import com.workfort.pstuian.app.ui.faculty.adapter.PagerAdapter
import com.workfort.pstuian.app.ui.home.viewstate.SignInUserState
import com.workfort.pstuian.app.ui.signup.viewstate.SignOutState
import com.workfort.pstuian.app.ui.studentprofile.viewstate.ChangeProfileInfoState
import com.workfort.pstuian.app.ui.teacherprofile.viewmodel.TeacherProfileViewModel
import com.workfort.pstuian.app.ui.webview.WebViewActivity
import com.workfort.pstuian.databinding.ActivityTeacherProfileBinding
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

class TeacherProfileActivity : BaseActivity<ActivityTeacherProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTeacherProfileBinding
            = ActivityTeacherProfileBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    override fun observeBroadcast() = Const.IntentAction.NOTIFICATION
    override fun onBroadcastReceived(intent: Intent) {
        handleNotificationIntent(intent)
    }

    private val mViewModel by viewModel<TeacherProfileViewModel>()
    private val mAuthViewModel : AuthViewModel by viewModel()
    private val mFileHandlerVM : FileHandlerViewModel by viewModel()

    private lateinit var mFaculty: FacultyEntity
    private lateinit var mTeacher: TeacherEntity
    private var isSignedIn = false

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

        observeSignedInUser()
        observeProfileImageChange()
        observeNameChange()
        observeBioChange()
        observeSignOut()
        observePasswordChange()
        lifecycleScope.launch {
            mAuthViewModel.intent.send(AuthIntent.GetSignInUser)
        }
    }

    private fun setUiData() {
        title = mTeacher.name
        with(binding.header) {
            btnCall.setOnClickListener { promptCall(mTeacher.phone) }
            btnEmail.setOnClickListener { promptEmail(mTeacher.email) }
            btnSignOut.setOnClickListener { promptSignOut() }
        }
        with(binding.content) {
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

            btnCamera.setOnClickListener { chooseImage() }
            btnEditBio.setOnClickListener { promptChangeBio(mTeacher) }
            btnEdit.setOnClickListener {
                val action = if(tabs.selectedTabPosition == 0) {
                    Const.Key.EDIT_ACADEMIC
                } else Const.Key.EDIT_CONNECT

                gotToEditProfile(mTeacher, action)
            }
        }
    }

    private fun initTabs() {
        pagerAdapter = PagerAdapter(this)
        pagerAdapter.addItem(createAcademicFragment())
        pagerAdapter.addItem(createConnectFragment())
        if(isSignedIn) pagerAdapter.addItem(createOptionFragment())
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
                if(!isSignedIn) return
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
                    ProfileInfoAction.EDIT -> {
                        when(item.actionData) {
                            mTeacher.name -> { promptChangeName(mTeacher) }
                            else -> Unit
                        }
                    }
                    else -> Unit
                }
            }
        })

    private fun createConnectFragment() = ProfilePagerItemFragment.instance(getConnectInfoList(),
        object: ProfileInfoClickEvent() {
            override fun onAction(item: ProfileInfoItem) {
                when(item.action) {
                    ProfileInfoAction.CALL -> { promptCall(item.actionData) }
                    ProfileInfoAction.MAIL -> { promptEmail(item.actionData) }
                    ProfileInfoAction.LINK -> item.actionData?.let { openLink(it) }
                    else -> Unit
                }
            }
        })

    private fun createOptionFragment() = ProfilePagerItemFragment.instance(getOptionList(),
        object : ProfileInfoClickEvent() {
            override fun onAction(item: ProfileInfoItem) {
                when (item.action) {
                    ProfileInfoAction.PASSWORD -> { promptChangePassword() }
                    else -> Unit
                }
            }
        })

    private fun getAcademicInfoList(): List<ProfileInfoItem> {
        val nameEntity = ProfileInfoItem(getString(R.string.txt_name), mTeacher.name)
        if(isSignedIn) {
            nameEntity.actionIcon = R.drawable.ic_pencil_circular_outline
            nameEntity.action = ProfileInfoAction.EDIT
            nameEntity.actionData = mTeacher.name
        }
        return listOf(
            nameEntity,
            ProfileInfoItem(getString(R.string.txt_designation), mTeacher.designation),
            ProfileInfoItem(getString(R.string.txt_department), mTeacher.department),
            ProfileInfoItem(getString(R.string.txt_faculty), mFaculty.title),
            ProfileInfoItem(getString(R.string.txt_blood_group), mTeacher.blood?: "~"),
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
                R.drawable.ic_web, ProfileInfoAction.LINK, mTeacher.linkedIn),
            ProfileInfoItem(getString(R.string.txt_facebook), mTeacher.fbLink?: "~",
                R.drawable.ic_web, ProfileInfoAction.LINK, mTeacher.fbLink),
        )
    }

    private fun getOptionList(): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem(
                getString(R.string.txt_password), getString(R.string.txt_change_password),
                R.drawable.ic_pencil_circular_outline, ProfileInfoAction.PASSWORD, "~"
            ),
        )
    }

    private fun observeSignedInUser() {
        lifecycleScope.launch {
            mAuthViewModel.signInUserState.collect {
                when (it) {
                    is SignInUserState.Idle -> setSignInUiState(isSignedIn = false)
                    is SignInUserState.Loading -> Unit
                    is SignInUserState.User -> {
                        isSignedIn = it.user is TeacherEntity && it.user.id == mTeacher.id
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
    }

    private val permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
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
            compressProfileImage(imgUri)
        })
    }

    // the compressed file should be stored in the cache folder
    private fun compressProfileImage(uri: Uri) {
        val fileName = "${mTeacher.id}.jpeg"
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
                    CommonDialog.error(this, "Failed to resize the image")
                }
                else -> setActionUiState(isActionRunning = false)
            }
        })
    }

    // The file should be exist in the cache directory
    private fun uploadProfileImage(fileName: String) {
        mFileHandlerVM.uploadImage(this, Const.Params.UserType.TEACHER, fileName)
            .observe(this, {
            when (it.state) {
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING -> {
                    val progress = it.progress.getInt(Const.Key.PROGRESS, 0)
                    if (progress == 0) {
                        setActionUiState(isActionRunning = true, isLoaderIndeterminate = false)
                    }
                    binding.content.loaderLarger.progress = progress
                }
                WorkInfo.State.SUCCEEDED -> {
                    setActionUiState(isActionRunning = false)
                    val data = it.outputData.getString(Const.Key.URL)
                    if(data.isNullOrEmpty()) {
                        CommonDialog.error(this, message = "Failed to upload the image")
                    } else {
                        mViewModel.changeProfileImage(mTeacher, data)
                    }
                }
                WorkInfo.State.FAILED -> {
                    setActionUiState(isActionRunning = false)
                    val data = it.outputData.getString(Const.Key.MESSAGE)
                    val message = if(data.isNullOrEmpty())
                        getString(R.string.default_error_dialog_message) else data
                    CommonDialog.error(this, "Upload Failed", message)
                }
                else -> setActionUiState(isActionRunning = false)
            }
        })
    }

    private fun observeProfileImageChange() {
        lifecycleScope.launch {
            mViewModel.changeProfileImageState.collect {
                when (it) {
                    is ChangeProfileInfoState.Idle -> Unit
                    is ChangeProfileInfoState.Loading -> setActionUiState(isActionRunning = true)
                    is ChangeProfileInfoState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        CommonDialog.success(this@TeacherProfileActivity)
                    }
                    is ChangeProfileInfoState.Error -> {
                        setActionUiState(isActionRunning = false)
                        setUiData()
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@TeacherProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    private var changeNameDialog: AlertDialog? = null
    private fun promptChangeName(teacher: TeacherEntity) {
        changeNameDialog = CommonDialog.changeName(this, teacher.name, { newName ->
            changeNameDialog?.dismiss()
            mViewModel.changeName(teacher, newName)
        })
    }

    private fun observeNameChange() {
        lifecycleScope.launch {
            mViewModel.changeNameState.collect {
                when (it) {
                    is ChangeProfileInfoState.Idle -> Unit
                    is ChangeProfileInfoState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is ChangeProfileInfoState.Success<*> -> {
                        setActivityResult()
                        mTeacher.name = it.data as String
                        title = it.data
                        //notify name change in academic tab
                        initTabs()
                        setActionUiState(isActionRunning = false)
                        CommonDialog.success(this@TeacherProfileActivity)
                    }
                    is ChangeProfileInfoState.Error -> {
                        setActionUiState(isActionRunning = false)
                        setUiData()
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@TeacherProfileActivity, message = msg)
                    }
                }
            }
        }
    }

    private var changeBioDialog: AlertDialog? = null
    private fun promptChangeBio(teacher: TeacherEntity) {
        changeBioDialog = CommonDialog.changeBio(this, teacher.bio?: "", { newBio ->
            changeBioDialog?.dismiss()
            mViewModel.changeBio(teacher, newBio)
        })
    }

    private fun observeBioChange() {
        lifecycleScope.launch {
            mViewModel.changeBioState.collect {
                when (it) {
                    is ChangeProfileInfoState.Idle -> Unit
                    is ChangeProfileInfoState.Loading -> {
                        setActionUiState(isActionRunning = true)
                    }
                    is ChangeProfileInfoState.Success<*> -> {
                        setActivityResult()
                        mTeacher.bio = it.data as String
                        binding.content.tvBio.text = it.data
                        setActionUiState(isActionRunning = false)
                        CommonDialog.success(this@TeacherProfileActivity)
                    }
                    is ChangeProfileInfoState.Error -> {
                        setActionUiState(isActionRunning = false)
                        setUiData()
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@TeacherProfileActivity, message = msg)
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
                    is SignOutState.Idle -> Unit
                    is SignOutState.Loading -> setActionUiState(isActionRunning = true)
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
                        CommonDialog.error(this@TeacherProfileActivity, message = msg)
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

    private fun promptChangePassword() {
        CommonDialog.changePassword(this, { oldPassword, newPassword ->
            mAuthViewModel.changePassword(oldPassword, newPassword)
        })
    }

    private fun observePasswordChange() {
        lifecycleScope.launch {
            mAuthViewModel.changePasswordState.collect {
                when (it) {
                    is ChangeProfileInfoState.Idle -> Unit
                    is ChangeProfileInfoState.Loading -> setActionUiState(isActionRunning = true)
                    is ChangeProfileInfoState.Success<*> -> {
                        setActionUiState(isActionRunning = false)
                        CommonDialog.success(this@TeacherProfileActivity,
                            message = it.data as String)
                    }
                    is ChangeProfileInfoState.Error -> {
                        setActionUiState(isActionRunning = false)
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@TeacherProfileActivity, message = msg)
                    }
                }
            }
        }
    }

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
                if(mTeacher.bio.isNullOrEmpty()) {
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

    private fun gotToEditProfile(teacher: TeacherEntity, action: String) {
        val intent = Intent(this, EditTeacherProfileActivity::class.java)
            .apply {
                putExtra(Const.Key.TEACHER, teacher)
                putExtra(Const.Key.EDIT_ACTION, action)
            }
        startActivityForResult.launch(intent)
    }

    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            result?.data?.also {
                it.getParcelableExtra<TeacherEntity>(Const.Key.TEACHER)?.let { teacher ->
                    mTeacher = teacher
                }
                it.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)?.let { faculty ->
                    mFaculty = faculty
                }
            }
            setUiData()
            initTabs()
            setSignInUiState(isSignedIn)
            setActivityResult()
        }
    }

    private fun setActivityResult(dataChanged: Boolean = true) {
        val intent = Intent()
        intent.putExtra(Const.Key.TEACHER, mTeacher)
        intent.putExtra(Const.Key.UPDATED, dataChanged)
        setResult(Activity.RESULT_OK, intent)
    }
}
