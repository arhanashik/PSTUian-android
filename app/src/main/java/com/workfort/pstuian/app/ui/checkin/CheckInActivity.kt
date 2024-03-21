package com.workfort.pstuian.app.ui.checkin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.checkin.adapter.CheckInAdapter
import com.workfort.pstuian.app.ui.checkin.intent.CheckInIntent
import com.workfort.pstuian.app.ui.checkin.viewmodel.CheckInViewModel
import com.workfort.pstuian.app.ui.common.bottomsheet.MyCheckInDetailsBottomSheet
import com.workfort.pstuian.app.ui.common.dialog.CommonDialog
import com.workfort.pstuian.app.ui.common.locationpicker.LocationPickerDialogFragment
import com.workfort.pstuian.app.ui.common.locationpicker.intent.CheckInLocationIntent
import com.workfort.pstuian.app.ui.common.locationpicker.viewmodel.CheckInLocationViewModel
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.databinding.ActivityCheckInBinding
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.sharedpref.Prefs
import com.workfort.pstuian.util.helper.Toaster
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:58 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 12/13/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class CheckInActivity : BaseActivity<ActivityCheckInBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityCheckInBinding
            = ActivityCheckInBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar
    override fun getMenuId(): Int = R.menu.menu_search
    override fun getSearchMenuItemId(): Int = R.id.action_search
    override fun getSearchQueryHint(): String = getString(R.string.hint_search)

    override fun observeBroadcast() = Const.IntentAction.NOTIFICATION
    override fun onBroadcastReceived(intent: Intent) {
        handleNotificationIntent(intent)
    }

    private val mViewModel: CheckInViewModel by viewModel()
    private val mCheckInLocationViewModel: CheckInLocationViewModel by viewModel()
    private lateinit var mAdapter: CheckInAdapter
    private var mCheckInLocation: CheckInLocationEntity? = null

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        initDataList()

        with(binding) {
            setClickListener(binding.chipCheckIn, binding.chipLocation)
            srlReloadData.setOnRefreshListener { loadData() }
            btnRefresh.setOnClickListener { loadData() }
        }

        lifecycleScope.launch {
            searchQuery.collectLatest {
                delay(300)
                mAdapter.filter.filter(it)
            }
        }

        observeCheckInLocation()
        observeCheckInList()
        observeCheckIn()

        /**
         * Load default check in location - last selected check in location
         * If not selected yet show for Main Campus
         * After getting the location data call setUiData() and loadData()
         * */
        val lastCheckInLocationId = Prefs.lastShownCheckInLocationId
        loadCheckInLocation(
            if(lastCheckInLocationId == -1) NetworkConst.Params.CheckInLocation.MAIN_CAMPUS
            else lastCheckInLocationId
        )
    }

    override fun onClick(v: View?) {
        // don't allow any click action while loading data
        val isLoading = mViewModel.checkInListState.value == RequestState.Loading
        if(isLoading) return

        with(binding) {
            when(v) {
                chipCheckIn -> promptLocationForCheckIn()
                chipLocation -> promptLocationChange()
                else -> super.onClick(v)
            }
        }
    }

    private fun loadCheckInLocation(locationId: Int) {
        lifecycleScope.launch {
            mCheckInLocationViewModel.intent.send(CheckInLocationIntent.Get(locationId))
        }
    }

    private fun observeCheckInLocation() {
        lifecycleScope.launch {
            mCheckInLocationViewModel.checkInLocationState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUiState(true)
                    is RequestState.Success<*> -> {
                        setActionUiState(false)
                        changeLocationAndReload(it.data as CheckInLocationEntity)
                    }
                    is RequestState.Error -> {
                        setActionUiState(false)
                        CommonDialog.error(this@CheckInActivity, message = it.error.orEmpty())
                    }
                }
            }
        }
    }

    private fun setUiData() {
        mCheckInLocation?.let {
            title = it.name
            val checkInLocation = "Tap to Change <b>${it.name}</b>"
            binding.chipLocation.text = HtmlCompat.fromHtml(
                checkInLocation,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

    private var endOfData = false
    private fun initDataList() {
        mAdapter = CheckInAdapter({
            // create new
        }) {
            // show details
            promptCheckInAction(it)
        }
        binding.rvData.adapter = mAdapter
        val layoutManager = binding.rvData.layoutManager as LinearLayoutManager
        binding.rvData.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isLoading = mViewModel.checkInListState.value == RequestState.Loading
                if(isLoading || endOfData) {
                    return
                }

                val scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val loadMorePosition = mAdapter.data.size - 1
                if(scrollPosition == loadMorePosition) {
                    loadData(mViewModel.checkInListPage + 1)
                }
            }
        })
    }

    private fun loadData(page: Int = 1) {
        if(page == 1) {
            endOfData = false
            mAdapter.clear()
        }
        mViewModel.checkInListPage = page
        mCheckInLocation?.let { location ->
            lifecycleScope.launch {
                mViewModel.intent.send(CheckInIntent.GetAll(
                    locationId = location.id, page = page
                ))
            }
        }
    }

    private fun observeCheckInList() {
        lifecycleScope.launch {
            mViewModel.checkInListState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUiState(true)
                    is RequestState.Success<*> -> {
                        setActionUiState(false)
                        val  list = it.data as List<CheckInEntity>
                        endOfData = list.isEmpty()
                        renderData(list)
                    }
                    is RequestState.Error -> {
                        endOfData = true
                        setActionUiState(false)
                        renderData(emptyList())
                        binding.tvMessage.text = it.error?: "Can't load data"
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

    private fun renderData(data: List<CheckInEntity>) {
        mAdapter.addData(data.toMutableList())
        val noData = mAdapter.data.isEmpty()
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

    private fun promptLocationForCheckIn() {
        LocationPickerDialogFragment.show(supportFragmentManager) { location ->
            lifecycleScope.launch {
                mViewModel.intent.send(CheckInIntent.CheckIn(location.id))
            }
        }
    }

    private fun observeCheckIn() {
        lifecycleScope.launch {
            mViewModel.checkInState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUiState(true)
                    is RequestState.Success<*> -> {
                        setActionUiState(false)
                        loadCheckInLocation((it.data as CheckInEntity).locationId)
                    }
                    is RequestState.Error -> {
                        setActionUiState(false)
                        Toaster.show(it.error.orEmpty())
                    }
                }
            }
        }
    }

    private fun promptLocationChange() {
        LocationPickerDialogFragment.show(supportFragmentManager) { location ->
            changeLocationAndReload(location)
        }
    }

    private fun changeLocationAndReload(location: CheckInLocationEntity) {
        Prefs.lastShownCheckInLocationId = location.id
        mCheckInLocation = location
        setUiData()
        loadData()
    }

    private fun promptCheckInAction(item: CheckInEntity) {
        MyCheckInDetailsBottomSheet.show(supportFragmentManager, item, showAction = false) {
            //TODO - add actions
        }
    }
}
