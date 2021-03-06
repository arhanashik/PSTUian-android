package com.workfort.pstuian.app.ui.common.checkinlist

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
import com.workfort.pstuian.app.data.local.checkin.CheckInEntity
import com.workfort.pstuian.app.ui.checkin.intent.CheckInIntent
import com.workfort.pstuian.app.ui.checkin.viewmodel.CheckInViewModel
import com.workfort.pstuian.app.ui.checkin.viewstate.CheckInDeleteState
import com.workfort.pstuian.app.ui.checkin.viewstate.CheckInListState
import com.workfort.pstuian.app.ui.checkin.viewstate.CheckInState
import com.workfort.pstuian.app.ui.common.bottomsheet.CheckInAction
import com.workfort.pstuian.app.ui.common.bottomsheet.MyCheckInDetailsBottomSheet
import com.workfort.pstuian.app.ui.common.checkinlist.adapter.CheckInListAdapter
import com.workfort.pstuian.databinding.FragmentCheckInListBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 *  ****************************************************************************
 *  * Created by : arhan on 15 Dec, 2021 at 22:47.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/15.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class CheckInListDialogFragment (
    private val userId: Int,
    private val userType: String,
    private val isSignedIn: Boolean
) : DialogFragment() {
    private var _binding: FragmentCheckInListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mViewModel: CheckInViewModel by sharedViewModel()
    private lateinit var mAdapter: CheckInListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInListBinding.inflate(inflater)
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
            btnDismiss.setOnClickListener { dismiss() }
        }

        observeCheckInList()
        observePrivacyChange()
        observeDelete()
        loadData()
    }

    private var endOfData = false
    private fun initDataList() {
        mAdapter = CheckInListAdapter { promptCheckInAction(it) }
        with(binding) {
            rvData.adapter = mAdapter
            val layoutManager = rvData.layoutManager as LinearLayoutManager
            rvData.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val isLoading = mViewModel.userCheckInListState.value ==
                            CheckInListState.Loading
                    if(isLoading || endOfData) {
                        return
                    }

                    val scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val loadMorePosition = mAdapter.data.size - 1
                    if(scrollPosition == loadMorePosition) {
                        loadData(mViewModel.userCheckInListPage + 1)
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
        mViewModel.userCheckInListPage = page
        lifecycleScope.launch {
            mViewModel.intent.send(CheckInIntent.GetAllByUser(userId, userType, page))
        }
    }

    private fun observeCheckInList() {
        lifecycleScope.launch {
            mViewModel.userCheckInListState.collect {
                when(it) {
                    is CheckInListState.Idle -> Unit
                    is CheckInListState.Loading -> setActionUiState(true)
                    is CheckInListState.CheckInList -> {
                        setActionUiState(false)
                        endOfData = it.list.isEmpty()
                        renderData(it.list)
                    }
                    is CheckInListState.Error -> {
                        setActionUiState(false)
                        endOfData = true
                        binding.tvMessage.text = it.message
                        renderData(emptyList())
                    }
                }
            }
        }
    }

    private fun renderData(data: List<CheckInEntity>) {
        if(mViewModel.userCheckInListPage == 1) mAdapter.setData(data)
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

    private fun promptCheckInAction(item: CheckInEntity) {
        MyCheckInDetailsBottomSheet.show(childFragmentManager, item, showAction = isSignedIn) {
            when(it) {
                is CheckInAction.Update -> promptChangePrivacy(it.item)
                is CheckInAction.Delete -> promptDelete(it.item)
                else -> Unit
            }
        }
    }

    private fun promptChangePrivacy(item: CheckInEntity) {
        CommonDialog.changePrivacy(requireContext(), item) { newPrivacy ->
            lifecycleScope.launch {
                mViewModel.intent.send(CheckInIntent.UpdatePrivacy(item.id, newPrivacy))
            }
        }
    }

    private fun observePrivacyChange() {
        lifecycleScope.launch {
            mViewModel.checkInPrivacyState.collect {
                when(it) {
                    is CheckInState.Idle -> Unit
                    is CheckInState.Loading -> setActionUiState(true)
                    is CheckInState.Success -> {
                        setActionUiState(false)
                        mAdapter.update(it.data)
                    }
                    is CheckInState.Error -> {
                        setActionUiState(false)
                        CommonDialog.error(requireContext(), message = it.message)
                    }
                }
            }
        }
    }

    private fun promptDelete(item: CheckInEntity) {
        CommonDialog.confirmation(requireContext()) {
            lifecycleScope.launch {
                mViewModel.intent.send(CheckInIntent.Delete(item.id))
            }
        }
    }

    private fun observeDelete() {
        lifecycleScope.launch {
            mViewModel.checkInDeleteState.collect {
                when(it) {
                    is CheckInDeleteState.Idle -> Unit
                    is CheckInDeleteState.Loading -> setActionUiState(true)
                    is CheckInDeleteState.Success -> {
                        setActionUiState(false)
                        mAdapter.remove(it.itemId)
                        // update empty view
                        if(mAdapter.data.isEmpty()) {
                            renderData(emptyList())
                        }
                    }
                    is CheckInDeleteState.Error -> {
                        setActionUiState(false)
                        CommonDialog.error(requireContext(), message = it.message)
                    }
                }
            }
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
        private const val TAG = "CheckInListDialogFragment"

        private fun newInstance(userId: Int, userType: String, isSignedIn: Boolean) =
            CheckInListDialogFragment(userId, userType, isSignedIn)

        fun show(
            fragmentManager: FragmentManager,
            userId: Int,
            userType: String,
            isSignedIn: Boolean
        ): CheckInListDialogFragment {
            val dialog = newInstance(userId, userType, isSignedIn)
            // dialog.isCancelable = false
            dialog.show(fragmentManager, TAG)
            return dialog
        }
    }
}