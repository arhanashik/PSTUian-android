package com.workfort.pstuian.app.ui.common.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.faculty.adapter.BatchesAdapter
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.listener.BatchClickEvent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.faculty.viewstate.BatchesState
import com.workfort.pstuian.databinding.LayoutBatchSelectorBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 9:18 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class BatchSelectorBottomSheet(
    private val faculty: FacultyEntity,
    private val callback: BatchClickEvent? = null
) : BottomSheetDialogFragment() {

    private var _binding: LayoutBatchSelectorBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mViewModel: FacultyViewModel by viewModel()
    private lateinit var mAdapter: BatchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutBatchSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            (this as? BottomSheetDialog)?.behavior?.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                setCancelable(false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBatchList()
        observeBatches()
        mViewModel.facultyId = faculty.id
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetBatches)
        }
    }

    private fun initBatchList() {
        mAdapter = BatchesAdapter(false)
        mAdapter.setListener(object : BatchClickEvent {
            override fun onClickBatch(batch: BatchEntity) {
                dismiss()
                callback?.onClickBatch(batch)
            }
        })

        binding.rvBatches.setHasFixedSize(true)
        binding.rvBatches.adapter = mAdapter
    }

    private fun observeBatches() {
        lifecycleScope.launch {
            mViewModel.batchesState.collect {
                when (it) {
                    is BatchesState.Idle -> {
                    }
                    is BatchesState.Loading -> {
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                        binding.rvBatches.visibility = View.GONE
                    }
                    is BatchesState.Batches -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.rvBatches.visibility = View.VISIBLE
                        renderBatches(it.batches)
                    }
                    is BatchesState.Error -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.rvBatches.visibility = View.GONE
                        dismiss()
                        val title = it.error?: "Ops, No Data!"
                        val msg = getString(R.string.error_msg_server_issue)
                        CommonDialog.error(requireContext(), title, msg)
                    }
                }
            }
        }
    }

    private fun renderBatches(list: List<BatchEntity>) {
        mAdapter.setBatches(list.toMutableList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BatchSelectorBottomSheet"
    }
}