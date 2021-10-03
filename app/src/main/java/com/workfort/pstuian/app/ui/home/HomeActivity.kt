package com.workfort.pstuian.app.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.workfort.pstuian.BuildConfig
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
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
import com.workfort.pstuian.app.ui.home.viewstate.SliderState
import com.workfort.pstuian.app.ui.settings.SettingsActivity
import com.workfort.pstuian.app.ui.splash.SplashActivity
import com.workfort.pstuian.databinding.ActivityHomeBinding
import com.workfort.pstuian.databinding.PromptDonateBinding
import com.workfort.pstuian.databinding.PromptDonationMessageBinding
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.helper.PlayStoreUtil
import com.workfort.pstuian.util.helper.Toaster
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
            = ActivityHomeBinding::inflate

    private val mViewModel: HomeViewModel by viewModel()
    private val mDonorsViewModel: DonorsViewModel by viewModel()

    private lateinit var mSliderAdapter: SliderAdapter
    private lateinit var mAdapter: FacultyAdapter

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        initSlider()
        initFacultyList()
        setClickEvents()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
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
            btnAbout.setOnClickListener {
                startActivity(Intent(this@HomeActivity, SettingsActivity::class.java))
            }
            cardUniversityWebsite.setOnClickListener {
                linkUtil.openBrowser(getString(R.string.link_pstu_website))
            }
            cardDonationList.setOnClickListener {
                startActivity(Intent(this@HomeActivity, DonorsActivity::class.java))
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
                PlayStoreUtil(this@HomeActivity)
                    .rateMyApp(this@HomeActivity, BuildConfig.APPLICATION_ID)
            }
            btnClearData.setOnClickListener { clearData() }
        }
    }

    private fun observeSliders() {
        lifecycleScope.launch {
            //observe sliders
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
                        Timber.e(it.error?: "Can't load sliders")
                    }
                }
            }
        }
    }

    private fun renderSliders(sliders: List<SliderEntity>) {
        mSliderAdapter.setSliders(sliders.toMutableList())
    }

    private fun observeFaculties() {
        lifecycleScope.launch {
            //observe faculties
            mViewModel.facultyState.collect {
                when (it) {
                    is FacultyState.Idle -> {
                    }
                    is FacultyState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                        binding.rvFaculties.visibility = View.INVISIBLE
                    }
                    is FacultyState.Faculties ->
                    {
                        binding.loader.visibility = View.GONE
                        binding.rvFaculties.visibility = View.VISIBLE
                        renderFaculties(it.faculties)
                    }
                    is FacultyState.Error ->
                    {
                        binding.loader.visibility = View.GONE
                        binding.rvFaculties.visibility = View.INVISIBLE
                        Timber.e("Can't load faculties")
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
            .setPositiveButton(getString(R.string.label_got_it)){
                dialog, _ -> dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun donate() {
        val donationMessage = DataBindingUtil.inflate<PromptDonationMessageBinding>(
            layoutInflater, R.layout.prompt_donation_message, null, false)

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
            layoutInflater, R.layout.prompt_donate, null, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            donationView.tvDonationInfo.text = Html.fromHtml(Prefs.donateOption,
                Html.FROM_HTML_MODE_LEGACY)
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
                        Toaster.show(it.error?: "Donation failed!")
                    }
                }
            }
        }
    }

    private fun clearData() {
        val alertDialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.label_are_you_sure))
            .setMessage(getString(R.string.data_clear_message))
            .setPositiveButton(getString(R.string.label_clear_data)) {
                    dialog, _ ->
                dialog.dismiss()
                lifecycleScope.launch {
                    mViewModel.intent.send(HomeIntent.DeleteAllData)
                }
            }
            .setNegativeButton(getString(R.string.label_cancel)) {
                    dialog, _ -> dialog.dismiss()
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
                        startActivity(Intent(this@HomeActivity,
                            SplashActivity::class.java))
                    }
                    is DeleteAllState.Error -> {
                        donationDialog?.dismiss()
                        Toaster.show(it.error?: "Deletion failed!")
                    }
                }
            }
        }
    }
}
