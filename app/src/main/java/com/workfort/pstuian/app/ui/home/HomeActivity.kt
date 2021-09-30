package com.workfort.pstuian.app.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarteist.autoimageslider.SliderLayout
import com.smarteist.autoimageslider.SliderView
import com.workfort.pstuian.BuildConfig
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.appconst.AppConst
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.databinding.ActivityHomeBinding
import com.workfort.pstuian.databinding.PromptDonateBinding
import com.workfort.pstuian.databinding.PromptDonationMessageBinding
import com.workfort.pstuian.app.ui.home.donors.DonorsActivity
import com.workfort.pstuian.app.ui.home.faculty.FacultyActivity
import com.workfort.pstuian.app.ui.home.faculty.adapter.FacultyAdapter
import com.workfort.pstuian.app.ui.home.faculty.listener.FacultyClickEvent
import com.workfort.pstuian.app.ui.settings.SettingsActivity
import com.workfort.pstuian.app.ui.splash.SplashActivity
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.helper.PlayStoreUtil
import com.workfort.pstuian.util.remote.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class HomeActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var mViewModel: HomeViewModel

    private var disposable = CompositeDisposable()
    private val apiService by lazy {
        ApiClient.create()
    }

    private lateinit var mAdapter: FacultyAdapter
    private var triggeredLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        initSlider()
        initFacultyList()
        if(NetworkUtil.isNetworkAvailable()) loadDonationOptions()

        val linkUtil = LinkUtil(this)
        mBinding.tvUniversityWebsite.setOnClickListener {
            linkUtil.openBrowser(getString(R.string.link_pstu_website))
        }
        mBinding.tvDonationList.setOnClickListener {
            startActivity(Intent(this, DonorsActivity::class.java))
        }
        mBinding.tvGradingSystem.setOnClickListener { showGrading() }
        mBinding.tvAdmissionSeatPlan.setOnClickListener {
            linkUtil.openBrowser(getString(R.string.link_pstu_seat_plan))
        }
        mBinding.tvAdmissionHelp.setOnClickListener {
            linkUtil.openBrowser(getString(R.string.link_pstu_help_line))
        }
        mBinding.btnDonate.setOnClickListener { donate() }
        mBinding.btnRateApp.setOnClickListener {
            PlayStoreUtil(this).rateMyApp(this, BuildConfig.APPLICATION_ID)
        }
        mBinding.btnClearData.setOnClickListener { clearData() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initSlider() {
        if(mViewModel.getSliders().isEmpty()) {
            if(NetworkUtil.isNetworkAvailable()) loadSliders(true)
        }else {
            mBinding.imageSlider.setIndicatorAnimation(SliderLayout.Animations.DROP)
            //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM
            // or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            mBinding.imageSlider.scrollTimeInSec = 2

            mViewModel.getSliders().forEach {
                    slider ->
                val sliderView = SliderView(this)

                sliderView.imageUrl = slider.imageUrl
                sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                sliderView.setDescription(slider.title)

                sliderView.setOnSliderClickListener {
                    Toast.makeText(
                        this@HomeActivity, slider.title, Toast.LENGTH_SHORT
                    ).show()
                }

                mBinding.imageSlider.addSliderView(sliderView)
            }

            if(NetworkUtil.isNetworkAvailable()) loadSliders(false)
        }
    }

    private fun initFacultyList() {
        mAdapter = FacultyAdapter()
        mAdapter.setListener(object : FacultyClickEvent {
            override fun onClickFaculty(faculty: FacultyEntity) {
                val intent = Intent(this@HomeActivity, FacultyActivity::class.java)
                intent.putExtra(AppConst.Key.FACULTY, faculty)
                startActivity(intent)
            }
        })

        mBinding.rvFaculties.setHasFixedSize(true)
        mBinding.rvFaculties.layoutManager = LinearLayoutManager(this)
        mBinding.rvFaculties.adapter = mAdapter

        mViewModel.getFaculties().observe(this, Observer {
            if (it.isEmpty()) {
                if(NetworkUtil.isNetworkAvailable()) loadFaculties(true)
                else showToast(getString(R.string.internet_not_available_exception))
            } else {
                mAdapter.setFaculties(it.toMutableList())
                if (!triggeredLoading && NetworkUtil.isNetworkAvailable()) {
                    triggeredLoading = true
                    loadFaculties(false)
                }
            }
        })
    }

    private fun loadSliders(loadFresh: Boolean) {
        disposable.add(apiService.loadSliders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    mViewModel.insertSliders(it.sliders)
                    if(loadFresh) initSlider()
                } else {
                    if(loadFresh)
                        Toast.makeText(
                            this@HomeActivity, it.message, Toast.LENGTH_SHORT
                        ).show()
                }
            }, {
                Timber.e(it)
            })
        )
    }

    private fun loadFaculties(loadFresh: Boolean) {
        if (loadFresh) {
            mBinding.loader.visibility = View.VISIBLE
            mBinding.rvFaculties.visibility = View.INVISIBLE
            mBinding.tvMessage.visibility = View.INVISIBLE
        }

        disposable.add(
            apiService.loadFaculties()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (loadFresh) mBinding.loader.visibility = View.INVISIBLE
                    if (it.success) {
                        if (it.faculties.isNotEmpty()) {
                            if (loadFresh) mBinding.rvFaculties.visibility = View.VISIBLE
                            mViewModel.insertFaculties(it.faculties)
                        } else {
                            if (loadFresh) mBinding.tvMessage.visibility = View.VISIBLE
                        }
                    }else {
                        if(loadFresh) mBinding.tvMessage.visibility = View.VISIBLE
                    }
                }, {
                    Timber.e(it)
                    if (loadFresh) {
                        mBinding.loader.visibility = View.INVISIBLE
                        mBinding.tvMessage.visibility = View.VISIBLE
                        showToast(it.message.toString())
                    }
                })
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

        val alertDialog = AlertDialog.Builder(this)
            .setView(donationView.root)
            .create()

        donationView.btnSaveDonation.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable()) {
                val name = donationView.donationName.text.toString()
                val info = donationView.donationInfo.text.toString()
                val email = donationView.donationEmail.text.toString()
                val reference = donationView.donationReference.text.toString()

                saveDonation(name, info, email, reference, alertDialog)
            }else {
                showToast(getString(R.string.internet_not_available_exception))
            }
        }

        alertDialog.show()
    }

    private fun loadDonationOptions() {
        disposable.add(
            apiService.loadDonationOption()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.success) {
                        Prefs.donateOption = it.donationOption
                    }
                }, {
                    Timber.e(it)
                })
        )
    }

    private fun saveDonation(name: String, info: String, email: String, reference: String,
                             dialog: AlertDialog) {
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(info) || TextUtils.isEmpty(email)
            || TextUtils.isEmpty(reference)) {
            showToast(getString(R.string.required_field_missing_exception))
            return
        }else {
            showToast(getString(R.string.saving_donation_message))
        }

        disposable.add(
            apiService.saveDonation(name, info, email, reference)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dialog.dismiss()
                    showToast(it.message)
                    if (it.success) {
                        Prefs.donationId = it.donationId
                    }
                }, {
                    Timber.e(it)
                    dialog.dismiss()
                    showToast(it.message.toString())
                })
        )
    }

    private fun clearData() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.label_are_you_sure))
            .setMessage(getString(R.string.data_clear_message))
            .setPositiveButton(getString(R.string.label_clear_data)) {
                    dialog, _ ->
                dialog.dismiss()
                if(mViewModel.clearAllData()) {
                    finishAffinity()
                    startActivity(Intent(this, SplashActivity::class.java))
                }
            }
            .setNegativeButton(getString(R.string.label_cancel)) {
                    dialog, _ -> dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
