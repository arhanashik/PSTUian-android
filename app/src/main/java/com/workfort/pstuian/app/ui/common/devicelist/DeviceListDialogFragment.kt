package com.workfort.pstuian.app.ui.common.devicelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.device.DeviceEntity
import com.workfort.pstuian.app.ui.common.devicelist.adapter.DeviceAdapter
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.splash.viewstate.DevicesState
import com.workfort.pstuian.databinding.FragmentDeviceListBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 *  ****************************************************************************
 *  * Created by : arhan on 23 Dec, 2021 at 22:38.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/23.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */
class DeviceListDialogFragment (private val onClickSignOutAll : () -> Unit) : DialogFragment() {
    private var _binding: FragmentDeviceListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mViewModel: AuthViewModel by sharedViewModel()
    private lateinit var mAdapter: DeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceListBinding.inflate(inflater)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme_FullScreen
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initDataList()

        with(binding) {
            srlReloadData.setOnRefreshListener { loadData() }
            btnRefresh.setOnClickListener { loadData() }
            btnSignOutFromAllDevices.setOnClickListener {
                CommonDialog.confirmation(
                    requireContext(),
                    getString(R.string.txt_sign_out_from_all),
                    getString(R.string.msg_sign_out_from_all),
                    getString(R.string.txt_sign_out),
                ) {
                    dismiss()
                    onClickSignOutAll()
                }
            }
            btnDismiss.setOnClickListener { dismiss() }
        }

        observeCheckInList()
        loadData()
    }

    private var endOfData = false
    private fun initDataList() {
        mAdapter = DeviceAdapter {  }
        with(binding) {
            rvData.adapter = mAdapter
            val layoutManager = rvData.layoutManager as LinearLayoutManager
            rvData.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val isLoading = mViewModel.devicesState.value == DevicesState.Loading
                    if(isLoading || endOfData) {
                        return
                    }

                    val scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val loadMorePosition = mAdapter.data.size - 1
                    if(scrollPosition == loadMorePosition) {
                        loadData(mViewModel.devicesPage + 1)
                    }
                }
            })
        }
    }

    private fun loadData(page: Int = 1) {
        if(page == 1) {
            endOfData = false
            mAdapter.clear()
        }
        mViewModel.devicesPage = page
        lifecycleScope.launch {
            mViewModel.intent.send(AuthIntent.GetAllDevices(page))
        }
    }

    private fun observeCheckInList() {
        lifecycleScope.launch {
            mViewModel.devicesState.collect {
                when(it) {
                    is DevicesState.Idle -> Unit
                    is DevicesState.Loading -> setActionUiState(true)
                    is DevicesState.Success -> {
                        setActionUiState(false)
                        endOfData = it.data.isEmpty()
                        renderData(it.data)
                    }
                    is DevicesState.Error -> {
                        setActionUiState(false)
                        endOfData = true
                        binding.tvMessage.text = it.message
                        renderData(emptyList())
                    }
                }
            }
        }
    }

    private fun renderData(data: List<DeviceEntity>) {
        if(mViewModel.devicesPage == 1) mAdapter.setData(data)
        else mAdapter.addData(data)
        val noData = mAdapter.data.isEmpty()
        val visibility = if(noData) View.GONE else View.VISIBLE
        val inverseVisibility = if(noData) View.VISIBLE else View.GONE
        with(binding) {
            rvData.visibility = visibility
            lavError.visibility = inverseVisibility
            if(data.isEmpty()) lavError.playAnimation()
            tvMessage.visibility = inverseVisibility
            btnRefresh.visibility = inverseVisibility
        }
    }

    private fun setActionUiState(isLoading: Boolean) {
        with(binding) {
            srlReloadData.isRefreshing = isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "DeviceListDialogFragment"

        private fun newInstance(onClickSignOutAll : () -> Unit) =
            DeviceListDialogFragment(onClickSignOutAll)

        fun show(
            fragmentManager: FragmentManager,
            onClickSignOutAll : () -> Unit,
        ): DeviceListDialogFragment {
            val dialog = newInstance(onClickSignOutAll)
            dialog.show(fragmentManager, TAG)
            return dialog
        }
    }
}