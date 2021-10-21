package com.workfort.pstuian.app.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.donors.DonorsActivity
import com.workfort.pstuian.app.ui.donors.intent.DonorsIntent
import com.workfort.pstuian.app.ui.donors.viewmodel.DonorsViewModel
import com.workfort.pstuian.app.ui.donors.viewstate.DonationState
import com.workfort.pstuian.app.ui.faculty.FacultyActivity
import com.workfort.pstuian.app.ui.faculty.adapter.FacultyAdapter
import com.workfort.pstuian.app.ui.faculty.listener.FacultyClickEvent
import com.workfort.pstuian.app.ui.home.adapter.SliderAdapter
import com.workfort.pstuian.app.ui.home.intent.HomeIntent
import com.workfort.pstuian.app.ui.home.viewmodel.HomeViewModel
import com.workfort.pstuian.app.ui.home.viewstate.DeleteAllState
import com.workfort.pstuian.app.ui.home.viewstate.FacultyState
import com.workfort.pstuian.app.ui.home.viewstate.SignInUserState
import com.workfort.pstuian.app.ui.home.viewstate.SliderState
import com.workfort.pstuian.app.ui.signin.SignInActivity
import com.workfort.pstuian.app.ui.splash.SplashActivity
import com.workfort.pstuian.databinding.ActivityHomeBinding
import com.workfort.pstuian.databinding.PromptDonateBinding
import com.workfort.pstuian.databinding.PromptDonationMessageBinding
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.helper.PlayStoreUtil
import com.workfort.pstuian.util.helper.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
            = ActivityHomeBinding::inflate

    private val mViewModel: HomeViewModel by viewModel()
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
        observeDonation()
        observeDeleteAllData()
        lifecycleScope.launch {
            mViewModel.intent.send(HomeIntent.GetSliders)
            mViewModel.intent.send(HomeIntent.GetFaculties)
            mDonorsViewModel.intent.send(DonorsIntent.GetDonationOptions)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            mAuthViewModel.intent.send(AuthIntent.GetSignInUser)
        }
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
//        binding.rvFaculties.layoutManager = GridLayoutManager(this, 2)
        binding.rvFaculties.adapter = mAdapter
    }

    private fun setClickEvents() {
        val linkUtil = LinkUtil(this)
        with(binding) {
            btnSignInSignUp.setOnClickListener {
                launchActivity<SignInActivity> {  }
            }
            btnAccount.setOnClickListener {}
            cardUniversityWebsite.setOnClickListener {
                linkUtil.openBrowser(getString(R.string.link_pstu_website))
            }
            cardDonationList.setOnClickListener {
                launchActivity<DonorsActivity> {  }
            }
            cardGradingSystem.setOnClickListener { showGrading() }
            cardAdmissionSeatPlan.setOnClickListener {
                linkUtil.openBrowser(getString(R.string.link_pstu_seat_plan))
            }
            cardAdmissionHelp.setOnClickListener {
                linkUtil.openBrowser(getString(R.string.link_pstu_help_line))
            }
            btnDonate.setOnClickListener { donate() }
            btnRateApp.setOnClickListener {
                PlayStoreUtil(this@HomeActivity).openStore()
            }
            btnClearData.setOnClickListener { clearData() }
        }
    }

    private fun observeSignedInUser() {
        lifecycleScope.launch {
            mAuthViewModel.signInUserState.collect {
                when (it) {
                    is SignInUserState.Idle -> {
                    }
                    is SignInUserState.Loading -> {
                        binding.btnSignInSignUp.visibility = View.INVISIBLE
                        binding.btnAccount.visibility = View.INVISIBLE
                    }
                    is SignInUserState.User -> {
                        with(binding) {
                            mSignedInUser = it.user
                            btnSignInSignUp.visibility = View.INVISIBLE
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
                        binding.btnAccount.visibility = View.INVISIBLE
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
            mViewModel.facultyState.collect {
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

    private fun donate() {
        val donationMessage = DataBindingUtil.inflate<PromptDonationMessageBinding>(
            layoutInflater, R.layout.prompt_donation_message, null, false
        )

        val alertDialog = AlertDialog.Builder(this)
            .setView(donationMessage.root)
            .create()

        donationMessage.btnDonate.setOnClickListener {
            alertDialog.dismiss()
            showDonationOption()
        }

        alertDialog.show()
    }

    private fun showDonationOption() {
        val donationView = DataBindingUtil.inflate<PromptDonateBinding>(
            layoutInflater, R.layout.prompt_donate, null, false
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            donationView.tvDonationInfo.text = Html.fromHtml(
                Prefs.donateOption,
                Html.FROM_HTML_MODE_LEGACY
            )
        } else {
            donationView.tvDonationInfo.text = Html.fromHtml(Prefs.donateOption)
        }

        donationView.btnSaveDonation.setOnClickListener { }

        donationDialog = AlertDialog.Builder(this)
            .setView(donationView.root)
            .create()

        donationView.btnSaveDonation.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable()) {
                val name = donationView.donationName.text.toString()
                val info = donationView.donationInfo.text.toString()
                val email = donationView.donationEmail.text.toString()
                val reference = donationView.donationReference.text.toString()

                mDonorsViewModel.saveDonation(name, info, email, reference)
            }else {
                Toaster.show(getString(R.string.internet_not_available_exception))
            }
        }

        donationDialog?.show()
    }

    private var donationDialog: AlertDialog? = null
    private fun observeDonation() {
        lifecycleScope.launch {
            mDonorsViewModel.donationState.collect {
                when (it) {
                    is DonationState.Idle -> {
                    }
                    is DonationState.Loading -> {
                        Toaster.show(getString(R.string.saving_donation_message))
                    }
                    is DonationState.Success -> {
                        donationDialog?.dismiss()
                        Toaster.show(it.message)
                    }
                    is DonationState.Error -> {
                        donationDialog?.dismiss()
                        Toaster.show(it.error ?: "Donation failed!")
                    }
                }
            }
        }
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
                    is DeleteAllState.Idle -> {
                    }
                    is DeleteAllState.Loading -> {
                        Toaster.show("Deleting please wait....")
                    }
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
                        donationDialog?.dismiss()
                        Toaster.show(it.error ?: "Deletion failed!")
                    }
                }
            }
        }
    }
}
