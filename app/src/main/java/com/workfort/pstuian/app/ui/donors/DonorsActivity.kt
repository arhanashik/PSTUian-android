package com.workfort.pstuian.app.ui.donors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
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

    override fun observeBroadcast() = Const.IntentAction.NOTIFICATION
    override fun onBroadcastReceived(intent: Intent) {
        handleNotificationIntent(intent)
    }

    private val mViewModel: DonorsViewModel by viewModel()
    private lateinit var mAdapter: DonorsAdapter

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        initDonorsList()
        observeDonors()
        loadData()
        with(binding) {
            srlReloadData.setOnRefreshListener { loadData() }
            btnRefresh.setOnClickListener { loadData() }
            fabDonate.setOnClickListener { donate() }
            btnFirstDonor.setOnClickListener { donate() }
            lifecycleScope.launch {
                delay(1500)
                fabDonate.shrink()
            }
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
        binding.rvData.adapter = mAdapter
    }

    private fun loadData() {
        lifecycleScope.launch {
            mViewModel.intent.send(DonorsIntent.GetDonors)
        }
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
                        setActionUi(isLoading = false)
                        renderDonors(emptyList())
                        Timber.e(it.error?: "Can't load data")
                    }
                }
            }
        }
    }

    private fun setActionUi(isLoading: Boolean) {
        val visibility = if(isLoading) View.VISIBLE else View.GONE
        val inverseVisibility = if(isLoading) View.GONE else View.VISIBLE
        with(binding) {
            srlReloadData.isRefreshing = isLoading
            shimmerLayout.visibility = visibility
            if(isLoading) shimmerLayout.startShimmer()
            else shimmerLayout.stopShimmer()

            rvData.visibility = inverseVisibility
            lavError.visibility = inverseVisibility
            tvMessage.visibility = inverseVisibility
            btnRefresh.visibility = inverseVisibility
        }
    }

    private fun renderDonors(data: List<DonorEntity>) {
        val visibility = if(data.isEmpty()) View.GONE else View.VISIBLE
        val inverseVisibility = if(data.isEmpty()) View.VISIBLE else View.GONE
        with(binding) {
            rvData.visibility = visibility
            srlReloadData.visibility = visibility
            lavError.visibility = inverseVisibility
            if(data.isEmpty()) lavError.playAnimation()
            tvMessage.visibility = inverseVisibility
            btnRefresh.visibility = inverseVisibility
        }
        mAdapter.setDonors(data.toMutableList())
    }

    private fun donate() {
        launchActivity<DonateActivity>()
    }
}
