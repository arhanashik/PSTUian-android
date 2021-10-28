package com.workfort.pstuian.app.ui.donors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.donate.DonateActivity
import com.workfort.pstuian.app.ui.donors.adapter.DonorsAdapter
import com.workfort.pstuian.app.ui.donors.intent.DonorsIntent
import com.workfort.pstuian.app.ui.donors.viewmodel.DonorsViewModel
import com.workfort.pstuian.app.ui.donors.viewstate.DonorsState
import com.workfort.pstuian.app.ui.faculty.listener.DonorClickEvent
import com.workfort.pstuian.databinding.ActivityDonorsBinding
import com.workfort.pstuian.util.extension.launchActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class DonorsActivity : BaseActivity<ActivityDonorsBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDonorsBinding
        = ActivityDonorsBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar
    override fun getMenuId(): Int = R.menu.menu_search
    override fun getSearchMenuItemId(): Int = R.id.action_search
    override fun getSearchQueryHint(): String = getString(R.string.hint_search_donors)

    private val mViewModel: DonorsViewModel by viewModel()
    private lateinit var mAdapter: DonorsAdapter

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        initDonorsList()

        binding.btnFirstDonor.setOnClickListener { donate() }
        binding.fabDonate.setOnClickListener { donate() }

        observeDonors()
        lifecycleScope.launch {
            mViewModel.intent.send(DonorsIntent.GetDonors)
            delay(1500)
            binding.fabDonate.shrink()
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
                    is DonorsState.Idle -> Unit
                    is DonorsState.Loading -> setActionUi(true)
                    is DonorsState.Donors -> {
                        setActionUi(isLoading = false)
                        renderDonors(it.donors)
                    }
                    is DonorsState.Error -> {
                        setActionUi(isLoading = false, isError = true)
                        Timber.e(it.error?: "Can't load data")
                    }
                }
            }
        }
    }

    private fun setActionUi(isLoading: Boolean, isError: Boolean = false) {
        val visibility = if(isLoading) View.GONE else View.VISIBLE
        val inverseVisibility = if(isLoading) View.VISIBLE else View.GONE
        with(binding) {
            rvDonors.visibility = visibility
            lavError.visibility = visibility
            tvMessage.visibility = visibility

            shimmerLayout.visibility = inverseVisibility
            if(isLoading) shimmerLayout.startShimmer()
            else shimmerLayout.stopShimmer()

            if(isError) {
                rvDonors.visibility = View.GONE
                lavError.visibility = View.VISIBLE
                tvMessage.visibility = View.VISIBLE
            }
        }
    }

    private fun renderDonors(donors: List<DonorEntity>) {
        with(binding) {
            val visibility = if(donors.isEmpty()) View.GONE else View.VISIBLE
            val inverseVisibility = if(donors.isEmpty()) View.VISIBLE else View.GONE
            rvDonors.visibility = visibility
            lavError.visibility = inverseVisibility
            tvMessage.visibility = inverseVisibility
            btnFirstDonor.visibility = inverseVisibility
        }
        mAdapter.setDonors(donors.toMutableList())
    }

    private fun donate() {
        launchActivity<DonateActivity> {  }
    }
}
