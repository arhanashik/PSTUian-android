package com.workfort.pstuian.app.ui.settings

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.databinding.ActivitySettingsBinding
import com.workfort.pstuian.databinding.PromptDonateBinding
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.remote.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SettingsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySettingsBinding

    private var disposable = CompositeDisposable()
    private val apiService by lazy {
        ApiClient.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.btnDonate.setOnClickListener { showDonationOption() }
        mBinding.btnCallDev.setOnClickListener {
            LinkUtil(this).callTo(getString(R.string.dev_team_phone))
        }
        mBinding.btnFeedback.setOnClickListener {
            LinkUtil(this).sendEmail(getString(R.string.dev_team_email))
        }
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
