package com.workfort.pstuian.app.ui.donors

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.donors.intent.DonorsIntent
import com.workfort.pstuian.app.ui.donors.viewmodel.DonorsViewModel
import com.workfort.pstuian.app.ui.donors.viewstate.DonationState
import com.workfort.pstuian.app.ui.donors.viewstate.DonorsState
import com.workfort.pstuian.app.ui.donors.adapter.DonorsAdapter
import com.workfort.pstuian.app.ui.faculty.listener.DonorClickEvent
import com.workfort.pstuian.databinding.ActivityDonorsBinding
import com.workfort.pstuian.databinding.PromptDonateBinding
import com.workfort.pstuian.databinding.PromptDonationMessageBinding
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.helper.Toaster
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class DonorsActivity : BaseActivity<ActivityDonorsBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDonorsBinding
        = ActivityDonorsBinding::inflate

    override fun getMenuId(): Int = R.menu.menu_search
    override fun getSearchMenuItemId(): Int = R.id.action_search
    override fun getSearchQueryHint(): String = getString(R.string.hint_search_donors)

    private val mViewModel: DonorsViewModel by viewModel()
    private lateinit var mAdapter: DonorsAdapter

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        initDonorsList()

        binding.tvFirstDonor.setOnClickListener { donate() }
        binding.fabDonate.setOnClickListener { donate() }

        observeDonors()
        observeDonation()
        lifecycleScope.launch {
            mViewModel.intent.send(DonorsIntent.GetDonors)
        }
    }

    override fun onSearchQueryChange(searchQuery: String) {
        mAdapter.filter.filter(searchQuery)
    }

    private fun initDonorsList() {
        mAdapter = DonorsAdapter()
        mAdapter.setListener(object: DonorClickEvent {
            override fun onClickDonor(donor: DonorEntity) {

            }
        })

        binding.rvDonors.layoutManager = LinearLayoutManager(this)
        binding.rvDonors.adapter = mAdapter
    }

    private fun observeDonors() {
        lifecycleScope.launch {
            mViewModel.donorsState.collect {
                when (it) {
                    is DonorsState.Idle -> {
                    }
                    is DonorsState.Loading -> {
                        with(binding) {
                            loader.visibility = View.VISIBLE
                            rvDonors.visibility = View.INVISIBLE
                            tvMessage.visibility = View.INVISIBLE
                            tvFirstDonor.visibility = View.INVISIBLE
                        }
                    }
                    is DonorsState.Donors -> {
                        with(binding) {
                            loader.visibility = View.GONE
                            rvDonors.visibility = View.VISIBLE
                            tvMessage.visibility = View.GONE
                            tvFirstDonor.visibility = View.GONE
                        }
                        renderDonors(it.donors)
                    }
                    is DonorsState.Error -> {
                        with(binding) {
                            loader.visibility = View.GONE
                            rvDonors.visibility = View.GONE
                            tvMessage.visibility = View.VISIBLE
                            tvFirstDonor.visibility = View.VISIBLE
                        }
                        Timber.e(it.error?: "Can't load data")
                    }
                }
            }
        }
    }

    private fun renderDonors(donors: List<DonorEntity>) {
        mAdapter.setDonors(donors.toMutableList())
    }

    private fun donate() {
        val donationMessage = PromptDonationMessageBinding.inflate(layoutInflater,
            null, false)

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
        val binding = PromptDonateBinding.inflate(layoutInflater, null, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvDonationInfo.text = Html.fromHtml(
                Prefs.donateOption,
                Html.FROM_HTML_MODE_LEGACY)
        } else {
            binding.tvDonationInfo.text = Html.fromHtml(Prefs.donateOption)
        }

        binding.btnSaveDonation.setOnClickListener { }

        donationDialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .create()

        binding.btnSaveDonation.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable()) {
                val name = binding.donationName.text.toString()
                val info = binding.donationInfo.text.toString()
                val email = binding.donationEmail.text.toString()
                val reference = binding.donationReference.text.toString()

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
