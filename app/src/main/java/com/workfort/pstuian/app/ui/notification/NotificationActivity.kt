package com.workfort.pstuian.app.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.notification.NotificationEntity
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.base.callback.ItemClickEvent
import com.workfort.pstuian.app.ui.notification.adapter.NotificationAdapter
import com.workfort.pstuian.app.ui.notification.intent.NotificationIntent
import com.workfort.pstuian.app.ui.notification.viewmodel.NotificationViewModel
import com.workfort.pstuian.app.ui.notification.viewstate.NotificationsState
import com.workfort.pstuian.databinding.ActivityNotificationBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
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
 *  *
 *  * Last edited by : arhan on 2021/10/29.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityNotificationBinding
            = ActivityNotificationBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    override fun observeBroadcast() = Const.IntentAction.NOTIFICATION
    override fun onBroadcastReceived(intent: Intent) {
        handleNotificationIntent(intent, "Dismiss") {}
        lifecycleScope.launch {
            mViewModel.intent.send(NotificationIntent.GetAll)
        }
    }

    private val mViewModel: NotificationViewModel by viewModel()
    private lateinit var mAdapter : NotificationAdapter

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        initList()

        observeStudents()
        lifecycleScope.launch {
            mViewModel.intent.send(NotificationIntent.GetAll)
        }
    }

    private fun initList() {
        mAdapter = NotificationAdapter()
        mAdapter.setListener(object: ItemClickEvent<NotificationEntity> {
            override fun onClickItem(item: NotificationEntity) {
                showDetails(item)
            }
        })

        binding.rvNotification.adapter = mAdapter
    }

    private fun observeStudents() {
        lifecycleScope.launch {
            mViewModel.notificationsState.collect {
                when (it) {
                    is NotificationsState.Idle -> Unit
                    is NotificationsState.Loading -> {
                        setActionUiState(true)
                    }
                    is NotificationsState.Notifications -> {
                        setActionUiState(false)
                        Prefs.hasNewNotification = false
                        renderNotifications(it.notifications)
                    }
                    is NotificationsState.Error -> {
                        setActionUiState(false)
                        binding.tvMessage.text = it.error?: "Can't load data"
                    }
                }
            }
        }
    }

    private fun setActionUiState(isLoading: Boolean) {
        val visibility = if(isLoading) View.GONE else View.VISIBLE
        val inverseVisibility = if(isLoading) View.VISIBLE else View.GONE
        with(binding) {
            rvNotification.visibility = visibility
            lavError.visibility = visibility
            tvMessage.visibility = visibility
            shimmerLayout.visibility = inverseVisibility
            if(isLoading) shimmerLayout.startShimmer()
            else shimmerLayout.stopShimmer()
        }
    }

    private fun renderNotifications(data: List<NotificationEntity>) {
        val visibility = if(data.isEmpty()) View.GONE else View.VISIBLE
        val inverseVisibility = if(data.isEmpty()) View.VISIBLE else View.GONE
        with(binding) {
            rvNotification.visibility = visibility
            lavError.visibility = inverseVisibility
            tvMessage.visibility = inverseVisibility
        }
        mAdapter.setItems(data.reversed().toMutableList())
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
