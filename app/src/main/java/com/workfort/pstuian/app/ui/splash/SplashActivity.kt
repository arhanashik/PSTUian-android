package com.workfort.pstuian.app.ui.splash

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.dialog.CommonDialog
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.home.HomeActivity
import com.workfort.pstuian.app.ui.home.intent.HomeIntent
import com.workfort.pstuian.app.ui.home.viewmodel.HomeViewModel
import com.workfort.pstuian.databinding.ActivitySplashBinding
import com.workfort.pstuian.model.ConfigEntity
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.helper.PlayStoreUtil
import kotlinx.coroutines.delay
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
 *
 * -> After that, request for app config "requestConfig()" and observe result
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
    private val mPlayStoreUtil: PlayStoreUtil by lazy { PlayStoreUtil(this) }

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
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUiState(true)
                    is RequestState.Success<*> -> {
                        setActionUiState(false)
                        clearCache()
                    }
                    is RequestState.Error -> {
                        setActionUiState(false)
                        CommonDialog.error(
                            this@SplashActivity,
                            title = "Device Registration Error",
                            message = it.error.orEmpty(),
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
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                    }
                    is RequestState.Success<*> -> {
                        binding.loader.visibility = View.GONE
                        requestConfig()
                    }
                    is RequestState.Error -> {
                        binding.loader.visibility = View.GONE
                        Timber.e(it.error)
                        val msg = it.error?: "Couldn't clear cache"
                        CommonDialog.error(
                            this@SplashActivity,
                            title = "Cache Clear Error",
                            message = msg,
                            btnText = getString(R.string.txt_retry),
                            cancelable = false,
                        ) { clearCache() }
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
                    is RequestState.Idle -> {
                        binding.loader.visibility = View.INVISIBLE
                    }
                    is RequestState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                    }
                    is RequestState.Success<*> -> {
                        binding.loader.visibility = View.INVISIBLE
                        handleConfig(it.data as ConfigEntity)
                    }
                    is RequestState.Error -> {
                        binding.loader.visibility = View.INVISIBLE
                        val msg = it.error?: "Couldn't get app config"
                        CommonDialog.error(
                            this@SplashActivity,
                            title = "Configuration Error",
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
        if(config.forceUpdate != 0 && config.forceUpdateDone.not()) {
            CommonDialog.error(
                context = this,
                title = getString(R.string.title_force_update_dialog),
                message = getString(R.string.message_force_update_dialog),
                btnText = getString(R.string.txt_update),
                false,
            ) {
                mPlayStoreUtil.openStore()
                finish()
            }
            return
        }

        if(config.forceRefresh != 0 && config.forceRefreshDone.not()) {
            CommonDialog.error(
                context = this,
                title = getString(R.string.title_force_refresh_dialog),
                message = getString(R.string.message_force_refresh_dialog),
                btnText = getString(R.string.txt_refresh),
                false,
            ) { refreshData() }
            return
        }

        launchActivity<HomeActivity>()
        finish()
    }

    private fun refreshData() {
        mHomeViewModel.sliderStateForceRefresh = true
        lifecycleScope.launch {
            mHomeViewModel.intent.send(HomeIntent.DeleteAllData)
        }
    }

    private fun observeDeleteAllData() {
        lifecycleScope.launch {
            mHomeViewModel.deleteAllDataState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                    }
                    is RequestState.Success<*> -> {
                        binding.loader.visibility = View.INVISIBLE
                        finishAffinity()
                        launchActivity<SplashActivity> {}
                    }
                    is RequestState.Error -> {
                        binding.loader.visibility = View.INVISIBLE
                        val msg = it.error?: "Couldn't refresh app data"
                        CommonDialog.error(
                            this@SplashActivity,
                            title = "Data Clear Error",
                            message = msg,
                            btnText = getString(R.string.txt_retry),
                            cancelable = false
                        ) { refreshData() }
                    }
                }
            }
        }
    }

    private fun setActionUiState(isActionRunning: Boolean) {
        val inverseVisibility = if(isActionRunning) View.VISIBLE else View.GONE
        with(binding) {
            loader.visibility = inverseVisibility
        }
    }
}
