package com.workfort.pstuian.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.donate.DonateActivity
import com.workfort.pstuian.app.ui.donors.DonorsActivity
import com.workfort.pstuian.app.ui.donors.intent.DonorsIntent
import com.workfort.pstuian.app.ui.donors.viewmodel.DonorsViewModel
import com.workfort.pstuian.app.ui.faculty.FacultyActivity
import com.workfort.pstuian.app.ui.faculty.adapter.FacultyAdapter
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.listener.FacultyClickEvent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.faculty.viewstate.BatchState
import com.workfort.pstuian.app.ui.faculty.viewstate.FacultyState
import com.workfort.pstuian.app.ui.home.adapter.SliderAdapter
import com.workfort.pstuian.app.ui.home.intent.HomeIntent
import com.workfort.pstuian.app.ui.home.viewmodel.HomeViewModel
import com.workfort.pstuian.app.ui.home.viewstate.DeleteAllState
import com.workfort.pstuian.app.ui.home.viewstate.SignInUserState
import com.workfort.pstuian.app.ui.home.viewstate.SliderState
import com.workfort.pstuian.app.ui.notification.NotificationActivity
import com.workfort.pstuian.app.ui.settings.SettingsActivity
import com.workfort.pstuian.app.ui.signin.SignInActivity
import com.workfort.pstuian.app.ui.splash.SplashActivity
import com.workfort.pstuian.app.ui.studentprofile.StudentProfileActivity
import com.workfort.pstuian.app.ui.support.SupportActivity
import com.workfort.pstuian.app.ui.webview.WebViewActivity
import com.workfort.pstuian.databinding.ActivityHomeBinding
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.helper.PlayStoreUtil
import com.workfort.pstuian.util.helper.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
            = ActivityHomeBinding::inflate

    override fun observeBroadcast() = Const.IntentAction.NOTIFICATION
    override fun onBroadcastReceived(intent: Intent) {
        handleNotificationIntent(intent)
        checkNewNotificationState()
    }

    private val mViewModel: HomeViewModel by viewModel()
    private val mFacultyViewModel: FacultyViewModel by viewModel()
    private val mAuthViewModel: AuthViewModel by viewModel()
    private val mDonorsViewModel: DonorsViewModel by viewModel()

    private lateinit var mSliderAdapter: SliderAdapter
    private lateinit var mAdapter: FacultyAdapter

    private var mSignedInUser: StudentEntity? = null

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        initSlider()
        initFacultyList()
        setClickEvents()

        observeSignedInUser()
        observeSliders()
        observeFaculties()
        observeDeleteAllData()
        lifecycleScope.launch {
            mViewModel.intent.send(HomeIntent.GetSliders)
            mFacultyViewModel.intent.send(FacultyIntent.GetFaculties)
            mDonorsViewModel.intent.send(DonorsIntent.GetDonationOptions)
        }
    }

    override fun onResume() {
        super.onResume()
        /**
         * For some weird reason, the sign in user gets loaded. But after the faculties are
         * loaded the user is gone :D. So, for now, calling this function again after the
         * faculties are loaded
         * */
        loadSignInUser()

        /**
         * Check if there is any new notification
         * */
        checkNewNotificationState()
    }

    private fun loadSignInUser() {
        lifecycleScope.launch {
            mAuthViewModel.intent.send(AuthIntent.GetSignInUser)
        }
    }

    private fun checkNewNotificationState() {
        binding.btnNotification.setImageResource(
            if(Prefs.hasNewNotification) R.drawable.ic_bell_badge_filled_primary
            else R.drawable.ic_bell_filled
        )
    }

    private fun initSlider() {
        mSliderAdapter = SliderAdapter()
        with(binding.imageSlider) {
            setSliderAdapter(mSliderAdapter)
            //set indicator animation by using SliderLayout.IndicatorAnimations.
            //WORM/THIN_WORM/COLOR/DROP/FILL/NONE/SCALE/SCALE_DOWN/SLIDE/SWAP
            setIndicatorAnimation(IndicatorAnimationType.FILL)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            startAutoCycle()
        }
    }

    private fun initFacultyList() {
        mAdapter = FacultyAdapter()
        mAdapter.setListener(object : FacultyClickEvent {
            override fun onClickFaculty(faculty: FacultyEntity) {
                val intent = Intent(this@HomeActivity, FacultyActivity::class.java)
                intent.putExtra(Const.Key.FACULTY, faculty)
                startActivity(intent)
            }
        })

        binding.rvFaculties.setHasFixedSize(true)
        binding.rvFaculties.adapter = mAdapter
    }

    private fun setClickEvents() {
        val playStoreUtil = PlayStoreUtil(this@HomeActivity)
        with(binding) {
            btnSignInSignUp.setOnClickListener {
                launchActivity<SignInActivity> {  }
            }
            btnAccount.setOnClickListener { loadBatch() }
            btnNotification.setOnClickListener { launchActivity<NotificationActivity>() }
            cardUniversityWebsite.setOnClickListener {
                launchActivity<WebViewActivity>(
                    Pair(Const.Key.URL, getString(R.string.link_pstu_website)))
            }
            cardDonationList.setOnClickListener {
                launchActivity<DonorsActivity> {  }
            }
            cardGradingSystem.setOnClickListener { showGrading() }
            cardAdmissionSeatPlan.setOnClickListener {
                playStoreUtil.openStore(getString(R.string.app_id_pstu_seat_plan))
            }
            cardAdmissionHelp.setOnClickListener {
                playStoreUtil.openStore(getString(R.string.app_id_pstu_help_line))
            }
            cardHelp.setOnClickListener { launchActivity<SupportActivity>() }
            btnDonate.setOnClickListener { launchActivity<DonateActivity>() }
            btnRateApp.setOnClickListener {
                playStoreUtil.openStore()
            }
            btnClearData.setOnClickListener { clearData() }
            btnSettings.setOnClickListener { launchActivity<SettingsActivity>() }
        }
    }

    private fun observeSignedInUser() {
        lifecycleScope.launch {
            mAuthViewModel.signInUserState.collect {
                when (it) {
                    is SignInUserState.Idle -> {
                    }
                    is SignInUserState.Loading -> {
                        binding.btnSignInSignUp.visibility = View.GONE
                        binding.btnAccount.visibility = View.GONE
                    }
                    is SignInUserState.User -> {
                        with(binding) {
                            mSignedInUser = it.user
                            btnSignInSignUp.visibility = View.GONE
                            btnAccount.visibility = View.VISIBLE
                            if(it.user.imageUrl.isNullOrEmpty()) {
                                btnAccount.load(R.drawable.img_placeholder_profile)
                            } else {
                                btnAccount.load(it.user.imageUrl) {
                                    placeholder(R.drawable.img_placeholder_profile)
                                    error(R.drawable.img_placeholder_profile)
                                }
                            }
                        }
                    }
                    is SignInUserState.Error -> {
                        binding.btnSignInSignUp.visibility = View.VISIBLE
                        binding.btnAccount.visibility = View.GONE
                        Timber.e(it.error)
                    }
                }
            }
        }
    }

    private fun observeSliders() {
        lifecycleScope.launch {
            mViewModel.sliderState.collect {
                when (it) {
                    is SliderState.Idle -> {
                    }
                    is SliderState.Loading -> {
                        binding.sliderLoader.visibility = View.VISIBLE
                        binding.imageSlider.visibility = View.INVISIBLE
                    }
                    is SliderState.Sliders -> {
                        binding.sliderLoader.visibility = View.GONE
                        binding.imageSlider.visibility = View.VISIBLE
                        renderSliders(it.sliders)
                    }
                    is SliderState.Error -> {
                        binding.sliderLoader.visibility = View.GONE
                        binding.imageSlider.visibility = View.INVISIBLE
                        Timber.e(it.error ?: "Can't load sliders")
                    }
                }
            }
        }
    }

    private fun renderSliders(sliders: List<SliderEntity>) {
        mSliderAdapter.setSliders(sliders.toMutableList())
    }

    private fun observeFaculties() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.root_layout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        lifecycleScope.launch {
            mFacultyViewModel.facultyState.collect {
                when (it) {
                    is FacultyState.Idle -> {
                    }
                    is FacultyState.Loading -> {
                        constraintSet.connect(
                            R.id.tv_information_title,
                            ConstraintSet.TOP,
                            R.id.shimmer_layout,
                            ConstraintSet.BOTTOM,
                            0
                        )
                        constraintSet.applyTo(constraintLayout)
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                        binding.rvFaculties.visibility = View.GONE
                        binding.tvMessage.visibility = View.GONE
                    }
                    is FacultyState.Faculties -> {
                        constraintSet.connect(
                            R.id.tv_information_title,
                            ConstraintSet.TOP,
                            R.id.rv_faculties,
                            ConstraintSet.BOTTOM,
                            0
                        )
                        constraintSet.applyTo(constraintLayout)
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.tvMessage.visibility = View.GONE
                        binding.rvFaculties.visibility = View.VISIBLE
                        renderFaculties(it.faculties)
                        /**
                         * For some weird reason, the sign in user gets loaded. But after the
                         * faculties are loaded the user is gone :D. So, for now, calling this
                         * function again after the faculties are loaded
                         * */
                        loadSignInUser()
                    }
                    is FacultyState.Error -> {
                        constraintSet.connect(
                            R.id.tv_information_title,
                            ConstraintSet.TOP,
                            R.id.tv_message,
                            ConstraintSet.BOTTOM,
                            0
                        )
                        constraintSet.applyTo(constraintLayout)
                        binding.tvMessage.visibility = View.VISIBLE
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.rvFaculties.visibility = View.GONE
                        Timber.e("Can't load data")
                    }
                }
            }
        }
    }

    private fun renderFaculties(faculties: List<FacultyEntity>) {
        mAdapter.setFaculties(faculties.toMutableList())
    }

    private fun showGrading() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.label_grading_system))
            .setMessage(getString(R.string.grading_system))
            .setPositiveButton(getString(R.string.label_got_it)){ dialog, _ -> dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun clearData() {
        val alertDialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.label_are_you_sure))
            .setMessage(getString(R.string.data_clear_message))
            .setPositiveButton(getString(R.string.label_clear_data)) { dialog, _ ->
                dialog.dismiss()
                lifecycleScope.launch {
                    mViewModel.intent.send(HomeIntent.DeleteAllData)
                }
            }
            .setNegativeButton(getString(R.string.label_cancel)) { dialog, _ -> dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun observeDeleteAllData() {
        lifecycleScope.launch {
            mViewModel.deleteAllDataState.collect {
                when (it) {
                    is DeleteAllState.Idle -> Unit
                    is DeleteAllState.Loading -> Unit
                    is DeleteAllState.Success -> {
                        finishAffinity()
                        startActivity(
                            Intent(
                                this@HomeActivity,
                                SplashActivity::class.java
                            )
                        )
                    }
                    is DeleteAllState.Error -> {
                        Toaster.show(it.error ?: "Deletion failed!")
                    }
                }
            }
        }
    }

    private fun loadBatch() {
        observeBatch()
        mSignedInUser?.let { student ->
            mFacultyViewModel.getBatch(student.batchId)
        }
    }

    private fun observeBatch() {
        lifecycleScope.launch {
            mFacultyViewModel.batchState.collect {
                when (it) {
                    is BatchState.Idle -> Unit
                    is BatchState.Loading -> {
//                        Toaster.show("Getting batch....")
                    }
                    is BatchState.Batch -> {
                        openProfile(it.batch)
                    }
                    is BatchState.Error -> {
                        Toaster.show(it.error ?: "Failed to open profile!")
                    }
                }
            }
        }
    }

    private fun openProfile(batch: BatchEntity) {
        mSignedInUser?.let { student ->
            var faculty: FacultyEntity? = null
            mAdapter.getItems().filter { it.id == student.facultyId }.also {
                if(!it.isNullOrEmpty()) faculty = it[0]
            }
            val intent = Intent(this, StudentProfileActivity::class.java).apply {
                putExtra(Const.Key.FACULTY, faculty)
                putExtra(Const.Key.BATCH, batch)
                putExtra(Const.Key.STUDENT, student)
            }
            startActivity(intent)
        }
    }
}
