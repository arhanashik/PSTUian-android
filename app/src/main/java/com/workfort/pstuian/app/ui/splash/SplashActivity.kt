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
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.home.HomeActivity
import com.workfort.pstuian.app.ui.home.intent.HomeIntent
import com.workfort.pstuian.app.ui.home.viewmodel.HomeViewModel
import com.workfort.pstuian.app.ui.home.viewstate.DeleteAllState
import com.workfort.pstuian.app.ui.splash.viewstate.ClearCacheState
import com.workfort.pstuian.app.ui.splash.viewstate.ConfigState
import com.workfort.pstuian.app.ui.splash.viewstate.DeviceState
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

/**
 * Steps to follow in splash after "onCreate()"
 * -> Run splash animation by "loadAnimations()" and observe the animation progress
 *
 * -> After splash animation end, register/update device by "registerDevice()" and observe
 *    result in "observeDeviceRegistration()"
 *    -> If successful, continue to the next step
 *    -> If failed, show blocking dialog with retry option
 *
 * -> After that trigger "clearCache()" and observe progress in "observeClearCache()"
 *      -> If cache data clear failed, show blocking error message.
 *      -> If successful goto next step
 *
 * -> Check for app update "checkForUpdate()", and observe the result
 *      -> If update available
 *          -> If force update necessary, show blocking update option "requestForceUpdate()"
 *          -> If flexible update, show cancelable update option "requestFlexibleUpdate()"
 *               -> Check update result in "onActivityResult()".
 *               -> If failed, check for update again "checkForUpdate()".
 *               -> If successful, go to next step.
 *      -> If no update available, (or failed to check update), go to next step
 *
 * -> After app update, request for app config "requestConfig()" and observe result
 *    in "observeConfig()"
 *      -> If failed, show blocking dialog with retry option.
 *      -> If successful, check config in "handleConfig()"
 *          -> If data refresh is not necessary, go to Home Page
 *          -> If data refresh is necessary, show blocking dialog to refresh data in
 *             "refreshData()", and observe in "observeDeleteAllData()"
 *              -> If failed to refresh data, show blocking dialog to retry "refreshData()"
 *              -> If successful restart the app
 *
 *
 * Steps to follow in "onResume()"
 * -> Check if there is any downloaded update pending
 * -> If available, request to install the new update
 * -> If download in progress(or paused), request to continue the download in background
 * */

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var mSetLeftIn: AnimatorSet

    private val mHomeViewModel: HomeViewModel by viewModel()
    private val mAuthViewModel: AuthViewModel by viewModel()

    private val mUpdateUtil: InAppUpdateUtil by lazy { InAppUpdateUtil(this) }
    private var mUpdateListener: InstallStateUpdatedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadAnimations()
        changeCameraDistance()
        flipCard()

        observeDeviceRegistration()
        observeConfig()
        observeClearCache()
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
                binding.tvTitle.animateText(getText(R.string.app_name))
                lifecycleScope.launch {
                    delay(2500)
                    registerDevice()
                }
            }
        })
    }

    private fun registerDevice() {
        lifecycleScope.launch {
            mAuthViewModel.intent.send(AuthIntent.RegisterDevice)
        }
    }

    private fun observeDeviceRegistration() {
        lifecycleScope.launch {
            mAuthViewModel.deviceRegistrationState.collect {
                when (it) {
                    is DeviceState.Idle -> Unit
                    is DeviceState.Loading -> setActionUiState(true)
                    is DeviceState.Success -> {
                        setActionUiState(false)
                        clearCache()
                    }
                    is DeviceState.Error -> {
                        setActionUiState(false)
                        val msg = it.message?: "Couldn't register the device"
                        CommonDialog.error(
                            this@SplashActivity,
                            message = msg,
                            btnText = getString(R.string.txt_retry),
                            cancelable = false,
                        ) { registerDevice() }
                    }
                }
            }
        }
    }

    private fun clearCache() {
        lifecycleScope.launch {
            mHomeViewModel.intent.send(HomeIntent.ClearCache)
        }
    }

    private fun observeClearCache() {
        lifecycleScope.launch {
            mHomeViewModel.clearCache.collect {
                when (it) {
                    is ClearCacheState.Idle -> Unit
                    is ClearCacheState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                    }
                    is ClearCacheState.Success -> {
                        binding.loader.visibility = View.GONE
                        checkForUpdate()
                    }
                    is ClearCacheState.Error -> {
                        binding.loader.visibility = View.GONE
                        Timber.e(it.error)
                        val msg = it.error?: "Couldn't clear cache"
                        CommonDialog.error(
                            this@SplashActivity,
                            message = msg,
                            btnText = getString(R.string.txt_retry),
                            cancelable = false,
                        ) {clearCache()}
                    }
                }
            }
        }
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        binding.tvTitle.cameraDistance = scale
    }

    private fun flipCard() {
        mSetLeftIn.setTarget(binding.tvTitle)
        mSetLeftIn.start()
    }

    private fun observeConfig() {
        lifecycleScope.launch {
            mAuthViewModel.configState.collect {
                when (it) {
                    is ConfigState.Idle -> {
                        binding.loader.visibility = View.INVISIBLE
                    }
                    is ConfigState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                    }
                    is ConfigState.Success -> {
                        binding.loader.visibility = View.INVISIBLE
                        handleConfig(it.config)
                    }
                    is ConfigState.Error -> {
                        binding.loader.visibility = View.INVISIBLE
                        val msg = it.error?: "Couldn't get app config"
                        CommonDialog.error(
                            this@SplashActivity,
                            message = msg,
                            btnText = getString(R.string.txt_retry),
                            cancelable = false
                        ) { requestConfig() }
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
        CommonDialog.error(this, title, message, btnText, false) {
            refreshData()
        }
    }

    private fun refreshData() {
        lifecycleScope.launch {
            mHomeViewModel.intent.send(HomeIntent.DeleteAllData)
        }
    }

    private fun observeDeleteAllData() {
        lifecycleScope.launch {
            mHomeViewModel.deleteAllDataState.collect {
                when (it) {
                    is DeleteAllState.Idle -> {
                    }
                    is DeleteAllState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                    }
                    is DeleteAllState.Success -> {
                        binding.loader.visibility = View.INVISIBLE
                        finishAffinity()
                        launchActivity<SplashActivity> {}
                    }
                    is DeleteAllState.Error -> {
                        binding.loader.visibility = View.INVISIBLE
                        val msg = it.error?: "Couldn't refresh app data"
                        CommonDialog.error(
                            this@SplashActivity,
                            message = msg,
                            btnText = getString(R.string.txt_retry),
                            cancelable = false
                        ) { refreshData() }
                    }
                }
            }
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
                    binding.loader.visibility = View.VISIBLE
                }
                InstallStatus.DOWNLOADED -> {
                    binding.loader.visibility = View.GONE
                    mUpdateListener?.let { mUpdateUtil.removeListener(it) }
                    requestUpdateInstallPermission()
                }
                else -> {
                    binding.loader.visibility = View.GONE
                }
            }
        }
        val title = getString(R.string.title_flexible_update_dialog)
        val message = getString(R.string.message_flexible_update_dialog)
        val btnText = getString(R.string.txt_update)
        CommonDialog.success(this, title, message, btnText) {
            mUpdateUtil.requestUpdate(
                this@SplashActivity,
                appUpdateInfo,
                false,
                Const.RequestCode.IN_APP_UPDATE,
                mUpdateListener
            )
        }
    }

    private fun requestForceUpdate(appUpdateInfo: AppUpdateInfo) {
        val title = getString(R.string.title_force_update_dialog)
        val message = getString(R.string.message_force_update_dialog)
        val btnText = getString(R.string.txt_update)
        CommonDialog.error(this, title, message, btnText) {
            mUpdateUtil.requestUpdate(
                this@SplashActivity,
                appUpdateInfo,
                true,
                Const.RequestCode.IN_APP_UPDATE
            )
        }
    }

    private fun requestUpdateInstallPermission() {
        val title = getString(R.string.title_install_update_dialog)
        val message = getString(R.string.message_install_update_dialog)
        val btnText = getString(R.string.txt_install)
        CommonDialog.success(this, title, message, btnText, cancelable = false) {
            mUpdateUtil.completeUpdate()
        }
    }

    private fun setActionUiState(isActionRunning: Boolean) {
        val inverseVisibility = if(isActionRunning) View.VISIBLE else View.GONE
        with(binding) {
            loader.visibility = inverseVisibility
        }
    }
}
