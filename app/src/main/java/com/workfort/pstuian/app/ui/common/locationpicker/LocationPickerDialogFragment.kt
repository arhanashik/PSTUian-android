package com.workfort.pstuian.app.ui.common.locationpicker

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.checkinlocation.CheckInLocationEntity
import com.workfort.pstuian.app.ui.common.locationpicker.adpater.CheckInLocationAdapter
import com.workfort.pstuian.app.ui.common.locationpicker.intent.CheckInLocationIntent
import com.workfort.pstuian.app.ui.common.locationpicker.viewmodel.CheckInLocationViewModel
import com.workfort.pstuian.app.ui.common.locationpicker.viewstate.CheckInLocationListState
import com.workfort.pstuian.app.ui.common.locationpicker.viewstate.CheckInLocationState
import com.workfort.pstuian.databinding.FragmentLocationPickerBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 22:10.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/14.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class LocationPickerDialogFragment(
    private val onPickLocation : (location: CheckInLocationEntity) -> Unit
) : DialogFragment() {
    private var _binding: FragmentLocationPickerBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mViewModel: CheckInLocationViewModel by sharedViewModel()
    private lateinit var mAdapter: CheckInLocationAdapter

    private val queryTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            when {
                p0.isNullOrEmpty() || p0.trim().isEmpty() -> {
                    binding.tilQuery.error = null
                    lifecycleScope.launch {
                        mViewModel.checkInLocationSearchQuery.emit("")
                    }
                }
                p0.trim().length > 50 -> {
                    binding.tilQuery.error = "Query cannot be more than 50 character"
                }
                else -> {
                    binding.tilQuery.error = null
                    lifecycleScope.launch {
                        mViewModel.checkInLocationSearchQuery.emit(p0.trim().toString())
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationPickerBinding.inflate(inflater)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme_FullScreen
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initDataList()

        with(binding) {
            etQuery.addTextChangedListener(queryTextWatcher)
            btnDismiss.setOnClickListener { dismiss() }
        }

        observeCheckInLocationList()
        observeNewLocation()
        lifecycleScope.launch {
            mViewModel.checkInLocationSearchQuery.collectLatest { query ->
                delay(500)
                search(query)
            }
        }
    }

    private var endOfData = false
    private fun initDataList() {
        mAdapter = CheckInLocationAdapter({ createNewLocation() }) {
            onPickLocation(it)
            dismiss()
        }
        with(binding) {
            rvData.adapter = mAdapter
            val layoutManager = rvData.layoutManager as LinearLayoutManager
            rvData.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val isLoading = mViewModel.checkInLocationListState.value ==
                            CheckInLocationListState.Loading
                    if(isLoading || endOfData) {
                        return
                    }

                    val scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val loadMorePosition = mAdapter.data.size - 1
                    if(scrollPosition == loadMorePosition) {
                        lifecycleScope.launch {
                            mViewModel.checkInLocationSearchQuery.collectLatest { query ->
                                search(query, mViewModel.checkInLocationPage + 1)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun search(query: String, page: Int = 1) {
        if(page == 1) {
            endOfData = false
            mAdapter.clear()
        }
        mViewModel.checkInLocationPage = page
        lifecycleScope.launch {
            mViewModel.intent.send(CheckInLocationIntent.Search(query, page))
        }
    }

    /**
     * Clear the search query and new location(if any) on dismiss
     * */
    override fun onDismiss(dialog: DialogInterface) {
        mViewModel.resetSearchQueryAndNewLocation()
    }

    private fun observeCheckInLocationList() {
        lifecycleScope.launch {
            mViewModel.checkInLocationListState.collect {
                when(it) {
                    is CheckInLocationListState.Idle -> Unit
                    is CheckInLocationListState.Loading -> {}
                    is CheckInLocationListState.Success -> {
                        renderData(it.data)
                    }
                    is CheckInLocationListState.Error -> {
                        Timber.e(it.message)
                        if(mAdapter.data.isEmpty())
                            mAdapter.showDefaultView(true)
                    }
                }
            }
        }
    }

    private fun renderData(data: List<CheckInLocationEntity>) {
        mAdapter.showDefaultView(false)
        if(mViewModel.checkInLocationPage == 1) {
            mAdapter.setData(data)
        } else {
            mAdapter.addData(data)
        }
    }

    private fun createNewLocation() {
        val locationName = binding.etQuery.text.toString()
        if(locationName.isEmpty()) {
            binding.tilQuery.error = "*Required"
        }
        if(locationName.length > 50) {
            binding.tilQuery.error = "*Max length is 50"
        }
        binding.tilQuery.error = null

        lifecycleScope.launch {
            mViewModel.intent.send(CheckInLocationIntent.Create(locationName))
        }
    }

    private fun observeNewLocation() {
        lifecycleScope.launch {
            mViewModel.newCheckInLocationState.collect {
                when(it) {
                    is CheckInLocationState.Idle -> Unit
                    is CheckInLocationState.Loading -> {}
                    is CheckInLocationState.Success -> {
                        onPickLocation(it.data)
                        dismiss()
                    }
                    is CheckInLocationState.Error -> {
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
        private const val TAG = "LocationPickerDialogFragment"

        private fun newInstance(onPickLocation : (location: CheckInLocationEntity) -> Unit) =
            LocationPickerDialogFragment(onPickLocation)

        fun show(
            fragmentManager: FragmentManager,
            onPickLocation : (location: CheckInLocationEntity) -> Unit
        ): LocationPickerDialogFragment {
            val dialog = newInstance(onPickLocation)
            // dialog.isCancelable = false
            dialog.show(fragmentManager, TAG)
            return dialog
        }
    }
}