package com.workfort.pstuian.app.ui.blooddonationrequest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.blooddonationrequest.BloodDonationRequestEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.blooddonationrequest.adapter.BloodDonationRequestAdapter
import com.workfort.pstuian.app.ui.blooddonationrequest.intent.BloodDonationIntent
import com.workfort.pstuian.app.ui.blooddonationrequest.viewmodel.BloodDonationViewModel
import com.workfort.pstuian.app.ui.blooddonationrequest.viewstate.BloodDonationRequestsState
import com.workfort.pstuian.databinding.ActivityBloodDonationRequestBinding
import com.workfort.pstuian.util.extension.launchActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 20:00 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 12/10/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class BloodDonationRequestActivity : BaseActivity<ActivityBloodDonationRequestBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityBloodDonationRequestBinding
            = ActivityBloodDonationRequestBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar
    override fun getMenuId(): Int = R.menu.menu_search
    override fun getSearchMenuItemId(): Int = R.id.action_search
    override fun getSearchQueryHint(): String = getString(R.string.hint_search_donation_request)

    override fun observeBroadcast() = Const.IntentAction.NOTIFICATION
    override fun onBroadcastReceived(intent: Intent) {
        handleNotificationIntent(intent)
    }

    private val mViewModel: BloodDonationViewModel by viewModel()
    private lateinit var mAdapter: BloodDonationRequestAdapter

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        initDataList()
        observeBloodDonationRequests()
        loadData()
        with(binding) {
            srlReloadData.setOnRefreshListener { loadData() }
            btnRefresh.setOnClickListener { loadData() }
            fabRequestDonation.setOnClickListener { createDonationRequest() }
            lifecycleScope.launch {
                delay(2000)
                fabRequestDonation.shrink()
            }
        }
    }

    override fun onSearchQueryChange(searchQuery: String) {
        mAdapter.filter.filter(searchQuery)
    }

    private var endOfData = false
    private fun initDataList() {
        mAdapter = BloodDonationRequestAdapter()
        binding.rvData.adapter = mAdapter
        val layoutManager = binding.rvData.layoutManager as LinearLayoutManager
        binding.rvData.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isLoading = mViewModel.donationRequestsState.value ==
                        BloodDonationRequestsState.Loading
                if(isLoading || endOfData) {
                    return
                }

                val scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val loadMorePosition = mAdapter.itemCount - 1
                if(scrollPosition == loadMorePosition) {
                    loadData(mViewModel.donationRequestsPage + 1)
                }
            }
        })
    }

    private fun loadData(page: Int = 1) {
        if(page == 1) {
            endOfData = false
            mAdapter.clear()
        }
        mViewModel.donationRequestsPage = page
        lifecycleScope.launch {
            mViewModel.intent.send(BloodDonationIntent.GetAllDonationRequests(page))
        }
    }

    private fun observeBloodDonationRequests() {
        lifecycleScope.launch {
            mViewModel.donationRequestsState.collect {
                when (it) {
                    is BloodDonationRequestsState.Idle -> Unit
                    is BloodDonationRequestsState.Loading -> setActionUiState(true)
                    is BloodDonationRequestsState.DonationRequests -> {
                        endOfData = it.data.isEmpty()
                        setActionUiState(false)
                        renderData(it.data)
                    }
                    is BloodDonationRequestsState.Error -> {
                        endOfData = true
                        setActionUiState(false)
                        renderData(emptyList())
                        binding.tvMessage.text = it.message
                    }
                }
            }
        }
    }

    private fun setActionUiState(isLoading: Boolean) {
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

    private fun renderData(data: List<BloodDonationRequestEntity>) {
        mAdapter.addData(data.toMutableList())
        val noData = mAdapter.itemCount == 0
        val visibility = if(noData) View.GONE else View.VISIBLE
        val inverseVisibility = if(noData) View.VISIBLE else View.GONE
        with(binding) {
            rvData.visibility = visibility
            srlReloadData.visibility = visibility
            lavError.visibility = inverseVisibility
            if(data.isEmpty()) lavError.playAnimation()
            tvMessage.visibility = inverseVisibility
            btnRefresh.visibility = inverseVisibility
        }
    }

    private fun createDonationRequest() {
        launchActivity<CreateBloodDonationRequestActivity>()
    }
}
