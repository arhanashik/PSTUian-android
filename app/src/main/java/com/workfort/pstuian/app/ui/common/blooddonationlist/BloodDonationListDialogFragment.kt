package com.workfort.pstuian.app.ui.common.blooddonationlist

import android.content.DialogInterface
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
import com.workfort.pstuian.app.data.local.blooddonation.BloodDonationEntity
import com.workfort.pstuian.app.ui.blooddonationrequest.intent.BloodDonationIntent
import com.workfort.pstuian.app.ui.blooddonationrequest.viewmodel.BloodDonationViewModel
import com.workfort.pstuian.app.ui.blooddonationrequest.viewstate.BloodDonationState
import com.workfort.pstuian.app.ui.blooddonationrequest.viewstate.BloodDonationsState
import com.workfort.pstuian.app.ui.blooddonationrequest.viewstate.ItemDeleteState
import com.workfort.pstuian.app.ui.common.blooddonationlist.adapter.BloodDonationListAdapter
import com.workfort.pstuian.databinding.FragmentBloodDonationListBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : arhan on 16 Dec, 2021 at 3:37.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/16.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class BloodDonationListDialogFragment (
    private val userId: Int,
    private val userType: String,
    private val isSignedIn: Boolean
) : DialogFragment() {
    private var _binding: FragmentBloodDonationListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mViewModel: BloodDonationViewModel by sharedViewModel()
    private lateinit var mAdapter: BloodDonationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBloodDonationListBinding.inflate(inflater)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme_FullScreen
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initDataList()

        with(binding) {
            btnDismiss.setOnClickListener { dismiss() }
        }

        observeBloodDonationList()
        observeUpdate()
        observeDelete()
        loadData()
    }

    private var endOfData = false
    private fun initDataList() {
        mAdapter = BloodDonationListAdapter({ promptEdit(it) }) { promptDelete(it) }
        with(binding) {
            rvData.adapter = mAdapter
            val layoutManager = rvData.layoutManager as LinearLayoutManager
            rvData.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val isLoading = mViewModel.donationsState.value ==
                            BloodDonationsState.Loading
                    if(isLoading || endOfData) {
                        return
                    }

                    val scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val loadMorePosition = mAdapter.data.size - 1
                    if(scrollPosition == loadMorePosition) {
                        loadData(mViewModel.donationsPage + 1)
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
        mViewModel.donationsPage = page
        lifecycleScope.launch {
            mViewModel.intent.send(BloodDonationIntent.GetAllDonations(userId, userType, page))
        }
    }

    /**
     * Clear the data on dismiss
     * */
    override fun onDismiss(dialog: DialogInterface) {
//        mViewModel.resetSearchQueryAndNewLocation()
    }

    private fun observeBloodDonationList() {
        lifecycleScope.launch {
            mViewModel.donationsState.collect {
                when(it) {
                    is BloodDonationsState.Idle -> Unit
                    is BloodDonationsState.Loading -> {}
                    is BloodDonationsState.Donations -> {
                        renderData(it.data)
                    }
                    is BloodDonationsState.Error -> {
                        Timber.e(it.message)
                    }
                }
            }
        }
    }

    private fun renderData(data: List<BloodDonationEntity>) {
        if(mViewModel.donationsPage == 1) {
            mAdapter.setData(data)
        } else {
            mAdapter.addData(data)
        }
    }

    private fun promptEdit(item: BloodDonationEntity) {
        // TODO - edit
    }

    private fun observeUpdate() {
        lifecycleScope.launch {
            mViewModel.updateDonationState.collect {
                when(it) {
                    is BloodDonationState.Idle -> Unit
                    is BloodDonationState.Loading -> {}
                    is BloodDonationState.Success -> {
                        mAdapter.update(it.item)
                    }
                    is BloodDonationState.Error -> {
                        CommonDialog.error(requireContext(), message = it.message)
                    }
                }
            }
        }
    }

    private fun promptDelete(item: BloodDonationEntity) {
        CommonDialog.deleteConfirmation(requireContext()) {
            lifecycleScope.launch {
                mViewModel.intent.send(BloodDonationIntent.DeleteDonation(item.id))
            }
        }
    }

    private fun observeDelete() {
        lifecycleScope.launch {
            mViewModel.deleteDonationState.collect {
                when(it) {
                    is ItemDeleteState.Idle -> Unit
                    is ItemDeleteState.Loading -> {}
                    is ItemDeleteState.Success -> {
                        mAdapter.remove(it.itemId)
                    }
                    is ItemDeleteState.Error -> {
                        CommonDialog.error(requireContext(), message = it.message)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "BloodDonationListDialogFragment"

        private fun newInstance(userId: Int, userType: String, isSignedIn: Boolean) =
            BloodDonationListDialogFragment(userId, userType, isSignedIn)

        fun show(
            fragmentManager: FragmentManager,
            userId: Int,
            userType: String,
            isSignedIn: Boolean
        ): BloodDonationListDialogFragment {
            val dialog = newInstance(userId, userType, isSignedIn)
            dialog.show(fragmentManager, TAG)
            return dialog
        }
    }
}