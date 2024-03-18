package com.workfort.pstuian.app.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.base.callback.ItemClickEvent
import com.workfort.pstuian.app.ui.common.dialog.CommonDialog
import com.workfort.pstuian.app.ui.notification.adapter.NotificationAdapter
import com.workfort.pstuian.app.ui.notification.intent.NotificationIntent
import com.workfort.pstuian.app.ui.notification.viewmodel.NotificationViewModel
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.databinding.ActivityNotificationBinding
import com.workfort.pstuian.model.NotificationEntity
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.sharedpref.Prefs
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 20:33.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityNotificationBinding
            = ActivityNotificationBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    override fun observeBroadcast() = Const.IntentAction.NOTIFICATION
    override fun onBroadcastReceived(intent: Intent) {
        handleNotificationIntent(intent, "Dismiss") {}
        lifecycleScope.launch { loadData() }
    }

    private val mViewModel: NotificationViewModel by viewModel()
    private lateinit var mAdapter : NotificationAdapter

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        initList()
        observeNotifications()
        loadData()
        with(binding) {
            srlReloadData.setOnRefreshListener { loadData() }
            btnRefresh.setOnClickListener { loadData() }
        }
    }

    private var endOfData = false
    private fun initList() {
        mAdapter = NotificationAdapter()
        mAdapter.setListener(object: ItemClickEvent<NotificationEntity> {
            override fun onClickItem(item: NotificationEntity) {
                showDetails(item)
            }
        })
        binding.rvData.adapter = mAdapter
        val layoutManager = binding.rvData.layoutManager as LinearLayoutManager
        binding.rvData.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isLoading = mViewModel.notificationsState.value == RequestState.Loading
                if(isLoading || endOfData) {
                    return
                }

                val scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val loadMorePosition = mAdapter.itemCount - 1
                if(scrollPosition == loadMorePosition) {
                    loadData(mViewModel.notificationsPage + 1)
                }
            }
        })
    }

    private fun loadData(page: Int = 1) {
        if(page == 1) {
            endOfData = false
            mAdapter.clear()
        }
        mViewModel.notificationsPage = page
        lifecycleScope.launch {
            mViewModel.intent.send(NotificationIntent.GetAll(page))
        }
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            mViewModel.notificationsState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUiState(true)
                    is RequestState.Success<*> -> {
                        val list = it.data as List<NotificationEntity>
                        endOfData = list.isEmpty()
                        setActionUiState(false)
                        Prefs.hasNewNotification = false
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

    private fun renderData(data: List<NotificationEntity>) {
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

    private fun showDetails(data: NotificationEntity) {
        val iconRes = when(data.type) {
            Const.NotificationType.DEFAULT -> R.drawable.ic_bell_filled
            Const.NotificationType.BLOOD_DONATION -> R.drawable.ic_blood_drop
            Const.NotificationType.NEWS -> R.drawable.ic_newspaper
            Const.NotificationType.HELP -> R.drawable.ic_hand_heart
            else -> R.drawable.ic_bell_badge_filled
        }
        CommonDialog.success(
            this,
            data.title?: "Notification",
            data.message,
            icon = iconRes
        )
    }
}
