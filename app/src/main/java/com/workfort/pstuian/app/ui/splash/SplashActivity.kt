package com.workfort.pstuian.app.ui.splash

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.home.HomeActivity
import com.workfort.pstuian.app.ui.home.intent.HomeIntent
import com.workfort.pstuian.app.ui.home.viewmodel.HomeViewModel
import com.workfort.pstuian.app.ui.home.viewstate.DeleteAllState
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.splash.viewstate.ConfigState
import com.workfort.pstuian.databinding.ActivitySplashBinding
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.helper.InAppUpdateUtil
import com.workfort.pstuian.util.helper.Toaster
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SplashActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySplashBinding
    private lateinit var mSetLeftIn: AnimatorSet

    private val mHomeViewModel: HomeViewModel by viewModel()
    private val mAuthViewModel: AuthViewModel by viewModel()

    private val mUpdateUtil: InAppUpdateUtil by lazy { InAppUpdateUtil(this) }
    private var mUpdateListener: InstallStateUpdatedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loadAnimations()
        changeCameraDistance()
        flipCard()

        observeConfig()
        observeDeleteAllData()
    }

    override fun onResume() {
        super.onResume()
        mUpdateUtil.checkForUpdate(object: InAppUpdateUtil.AppUpdateCheckCallback() {
            override fun onDownloadedUpdateAvailable() {
                requestUpdateInstallPermission()
            }

            override fun onAlreadyInProgress(appUpdateInfo: AppUpdateInfo) {
                mUpdateUtil.requestUpdate(
                    this@SplashActivity,
                    appUpdateInfo,
                    true,
                    Const.RequestCode.IN_APP_UPDATE
                )
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Const.RequestCode.IN_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Toaster.show("Update failed, please try again!")
                Timber.e("Update flow failed! Result code: $resultCode")
                checkForUpdate()
            } else {
                Toaster.show("App Updated Successfully!")
                requestConfig()
            }
        }
    }

    private fun loadAnimations() {
        mSetLeftIn = AnimatorInflater.loadAnimator(this, R.animator.flip_in) as AnimatorSet
        mSetLeftIn.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationStart(animation: Animator) {
                //mBinding.tvTitle.setAnimationListener { SimpleAnimationListener(this@SplashActivity) }
                mBinding.tvTitle.animateText(getText(R.string.app_name))
                lifecycleScope.launch {
                    delay(2500)
                    checkForUpdate()
                }
            }
        })
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        mBinding.tvTitle.cameraDistance = scale
    }

    private fun flipCard() {
        mSetLeftIn.setTarget(mBinding.tvTitle)
        mSetLeftIn.start()
    }

    private fun observeConfig() {
        lifecycleScope.launch {
            mAuthViewModel.configState.collect {
                when (it) {
                    is ConfigState.Idle -> {
                        mBinding.loader.visibility = View.INVISIBLE
                    }
                    is ConfigState.Loading -> {
                        mBinding.loader.visibility = View.VISIBLE
                    }
                    is ConfigState.Success -> {
                        mBinding.loader.visibility = View.INVISIBLE
                        handleConfig(it.config)
                    }
                    is ConfigState.Error -> {
                        mBinding.loader.visibility = View.INVISIBLE
                        Timber.e(it.error?: "Can't load data")
                    }
                }
            }
        }
    }

    private fun requestConfig() {
        lifecycleScope.launch {
            mAuthViewModel.intent.send(AuthIntent.GetConfig)
        }
    }

    private fun handleConfig(config: ConfigEntity) {
        if(config.forceRefresh == 0 || config.forceRefreshDone) {
            launchActivity<HomeActivity>()
            finish()
            return
        }

        val title = getString(R.string.title_force_refresh_dialog)
        val message = getString(R.string.message_force_refresh_dialog)
        val btnText = getString(R.string.txt_refresh)
        val onBtnClick = ::refreshData
        CommonDialog.error(this, title, message, btnText, false, onBtnClick)
    }

    private fun refreshData() {
        lifecycleScope.launch {
            mHomeViewModel.intent.send(HomeIntent.DeleteAllData)
        }
    }

    private fun checkForUpdate() {
        mUpdateUtil.checkForUpdate(object: InAppUpdateUtil.AppUpdateCheckCallback() {
            override fun onUpdated() {
                //app is UP-TO-DATE. So, continue normal operation
                requestConfig()
            }
            override fun onAvailable(
                appUpdateInfo: AppUpdateInfo,
                needForceUpdate: Boolean,
                isImmediate: Boolean
            ) {
                if(needForceUpdate) {
                    requestForceUpdate(appUpdateInfo)
                }else {
                    requestFlexibleUpdate(appUpdateInfo)
                }
            }

            override fun onAlreadyInProgress(appUpdateInfo: AppUpdateInfo) {
                mUpdateUtil.requestUpdate(
                    this@SplashActivity,
                    appUpdateInfo,
                    true,
                    Const.RequestCode.IN_APP_UPDATE
                )
            }

            override fun onError(exception: Exception) {
                Timber.e(exception)
                Toaster.show("Ops, failed to check for update")
                // App update check is failed, so just continue without it
                requestConfig()
            }
        })
    }

    private fun requestFlexibleUpdate(appUpdateInfo: AppUpdateInfo) {
        mUpdateListener = InstallStateUpdatedListener { state ->
            when(state.installStatus()) {
                InstallStatus.DOWNLOADING -> {
//                    val bytesDownloaded = state.bytesDownloaded()
//                    val totalBytesToDownload = state.totalBytesToDownload()
                    // Show update progress bar.
                }
                InstallStatus.DOWNLOADED -> {
                    mUpdateListener?.let { mUpdateUtil.removeListener(it) }
                    requestUpdateInstallPermission()
                }
                else -> {

                }
            }
        }
        val title = getString(R.string.title_flexible_update_dialog)
        val message = getString(R.string.message_flexible_update_dialog)
        val btnText = getString(R.string.txt_update)
        CommonDialog.success(this, title, message, btnText,
            callback = object: CommonDialog.SuccessDialogCallback {
            override fun onClickDismiss() {
                mUpdateUtil.requestUpdate(
                    this@SplashActivity,
                    appUpdateInfo,
                    false,
                    Const.RequestCode.IN_APP_UPDATE,
                    mUpdateListener
                )
            }
        })
    }

    private fun requestForceUpdate(appUpdateInfo: AppUpdateInfo) {
        val title = getString(R.string.title_force_update_dialog)
        val message = getString(R.string.message_force_update_dialog)
        val btnText = getString(R.string.txt_update)
        CommonDialog.error(this, title, message, btnText,
            callback = object: CommonDialog.ErrorDialogCallback {
                override fun onClickDismiss() {
                    mUpdateUtil.requestUpdate(
                        this@SplashActivity,
                        appUpdateInfo,
                        true,
                        Const.RequestCode.IN_APP_UPDATE
                    )
                }
            })
    }

    private fun requestUpdateInstallPermission() {
        val title = getString(R.string.title_install_update_dialog)
        val message = getString(R.string.message_install_update_dialog)
        val btnText = getString(R.string.txt_install)
        CommonDialog.success(this, title, message, btnText, cancelable = false,
            callback = object: CommonDialog.SuccessDialogCallback {
                override fun onClickDismiss() {
                    mUpdateUtil.completeUpdate()
                }
            })
    }

    private fun observeDeleteAllData() {
        lifecycleScope.launch {
            mHomeViewModel.deleteAllDataState.collect {
                when (it) {
                    is DeleteAllState.Idle -> {
                    }
                    is DeleteAllState.Loading -> {
                        mBinding.loader.visibility = View.VISIBLE
                    }
                    is DeleteAllState.Success -> {
                        mBinding.loader.visibility = View.INVISIBLE
                        finishAffinity()
                        launchActivity<SplashActivity> {}
                    }
                    is DeleteAllState.Error -> {
                        mBinding.loader.visibility = View.INVISIBLE
                        Toaster.show(it.error ?: "Deletion failed!")
                    }
                }
            }
        }
    }

//    class SimpleAnimationListener(val context: Context) : AnimationListener {
//        override fun onAnimationEnd(hTextView: HTextView) {
//            Timber.e("working...")
//            Toast.makeText(context, "Animation finished", Toast.LENGTH_SHORT).show()
//        }
//    }
}
