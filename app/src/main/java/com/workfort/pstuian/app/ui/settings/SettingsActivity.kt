package com.workfort.pstuian.app.ui.settings

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.donors.viewmodel.DonorsViewModel
import com.workfort.pstuian.app.ui.donors.viewstate.DonationState
import com.workfort.pstuian.databinding.ActivitySettingsBinding
import com.workfort.pstuian.databinding.PromptDonateBinding
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.helper.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity: BaseActivity<ActivitySettingsBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivitySettingsBinding
            = ActivitySettingsBinding::inflate

    private val mViewModel: DonorsViewModel by viewModel()

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        observeDonation()

        val linkUtil = LinkUtil(this@SettingsActivity)
        with(binding) {
            btnDonate.setOnClickListener { showDonationOption() }
            btnCallDev.setOnClickListener {
                linkUtil.callTo(getString(R.string.dev_team_phone))
            }
            btnFeedback.setOnClickListener {
                linkUtil.sendEmail(getString(R.string.dev_team_email))
            }
        }
        binding.btnDonate.setOnClickListener { showDonationOption() }
    }

    private fun showDonationOption() {
        val donationView = PromptDonateBinding.inflate(layoutInflater,null, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            donationView.tvDonationInfo.text = Html.fromHtml(Prefs.donateOption,
                Html.FROM_HTML_MODE_LEGACY)
        } else {
            donationView.tvDonationInfo.text = Html.fromHtml(Prefs.donateOption)
        }

        donationDialog = AlertDialog.Builder(this)
            .setView(donationView.root)
            .create()

        donationView.btnSaveDonation.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable()) {
                val name = donationView.donationName.text.toString()
                val info = donationView.donationInfo.text.toString()
                val email = donationView.donationEmail.text.toString()
                val reference = donationView.donationReference.text.toString()

                mViewModel.saveDonation(name, info, email, reference)
            }else {
                Toaster.show(getString(R.string.internet_not_available_exception))
            }
        }

        donationDialog?.show()
    }

    private var donationDialog: AlertDialog? = null
    private fun observeDonation() {
        lifecycleScope.launch {
            mViewModel.donationState.collect {
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
}
