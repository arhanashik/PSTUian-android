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
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.faculty.adapter.FacultyAdapter
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.faculty.viewstate.FacultyState
import com.workfort.pstuian.databinding.LayoutFacultySelectorBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

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

class FacultySelectorBottomSheet(
    private val onClickItem : (faculty: FacultyEntity) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: LayoutFacultySelectorBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mViewModel: FacultyViewModel by sharedViewModel()
    private lateinit var mAdapter: FacultyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutFacultySelectorBinding.inflate(inflater, container, false)
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

        initFacultyList()
        observeFaculties()
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetFaculties)
        }
    }

    private fun initFacultyList() {
        mAdapter = FacultyAdapter { faculty ->
            dismiss()
            onClickItem(faculty)
        }

        binding.rvFaculties.setHasFixedSize(true)
        binding.rvFaculties.adapter = mAdapter
    }

    private fun observeFaculties() {
        lifecycleScope.launch {
            mViewModel.facultyState.collect {
                when (it) {
                    is FacultyState.Idle -> {
                    }
                    is FacultyState.Loading -> {
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                        binding.rvFaculties.visibility = View.GONE
                    }
                    is FacultyState.Faculties -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.rvFaculties.visibility = View.VISIBLE
                        renderFaculties(it.faculties)
                    }
                    is FacultyState.Error -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.rvFaculties.visibility = View.GONE
                        val title = it.error?: "Ops, No Data!"
                        val msg = getString(R.string.error_msg_server_issue)
                        CommonDialog.error(requireContext(), title, msg)
                    }
                }
            }
        }
    }

    private fun renderFaculties(faculties: List<FacultyEntity>) {
        mAdapter.setData(faculties.toMutableList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FacultySelectorBottomSheet"
    }
}