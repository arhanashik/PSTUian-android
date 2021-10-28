package com.workfort.pstuian.app.ui.donate

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.donors.viewmodel.DonorsViewModel
import com.workfort.pstuian.app.ui.donors.viewstate.DonationState
import com.workfort.pstuian.databinding.ActivityDonateBinding
import com.workfort.pstuian.databinding.PromptDonationMessageBinding
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DonateActivity : BaseActivity<ActivityDonateBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDonateBinding
        = ActivityDonateBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private val mViewModel: DonorsViewModel by viewModel()

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        setUiData()
        observeDonation()
        binding.content.fabDonate.setOnClickListener { donate() }

        showInfoDialog()
    }

    private fun setUiData() {
        val optionsStr = "You can send a donation to:<br>" +
                (Prefs.donateOption?: "") + "<br>After that, please save the information."
        val options = HtmlCompat.fromHtml(optionsStr, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.content.tvDonationOption.text = options
    }

    private fun setActionUi(isLoading: Boolean) {
        val visibility = if(isLoading) View.VISIBLE else View.GONE
        with(binding) {
            loaderOverlay.visibility = visibility
            loader.visibility = visibility
            labelSaving.visibility = visibility
        }
    }

    private fun donate() {
        with(binding.content) {
            val reference = etReference.text.toString()
            if(TextUtils.isEmpty(reference)) {
                tilReference.error = "*Required"
                return
            }
            tilReference.error = ""
            var name = etName.text.toString()
            if(TextUtils.isEmpty(name)) name = "anonymous"
            var email = etEmail.text.toString()
            if(TextUtils.isEmpty(name)) email = "anonymous"
            var info = etInfo.text.toString()
            if(TextUtils.isEmpty(name)) info = "anonymous"

            mViewModel.saveDonation(name, info, email, reference)
        }
    }

    private fun observeDonation() {
        lifecycleScope.launch {
            mViewModel.donationState.collect {
                when (it) {
                    is DonationState.Idle -> Unit
                    is DonationState.Loading -> setActionUi(isLoading = true)
                    is DonationState.Success -> {
                        setActionUi(isLoading = false)
                        CommonDialog.success(this@DonateActivity, message = it.message)
                    }
                    is DonationState.Error -> {
                        setActionUi(isLoading = false)
                        val error = it.error?: "Donation failed!"
                        CommonDialog.error(this@DonateActivity, message = error)
                    }
                }
            }
        }
    }

    private fun showInfoDialog() {
        launchActivity<DonateActivity> {  }
        val donationMessage = DataBindingUtil.inflate<PromptDonationMessageBinding>(
            layoutInflater, R.layout.prompt_donation_message, null, false
        )

        val alertDialog = AlertDialog.Builder(this)
            .setView(donationMessage.root)
            .create()

        donationMessage.btnDonate.setOnClickListener {
            alertDialog.dismiss()
            launchActivity<DonateActivity> {  }
        }

        alertDialog.show()
    }
}
