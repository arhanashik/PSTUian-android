package com.workfort.pstuian.app.ui.common.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.checkin.CheckInEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.databinding.LayoutMyCheckInActionBinding
import com.workfort.pstuian.util.helper.DateUtil
import com.workfort.pstuian.util.helper.MathUtil

/**
 *  ****************************************************************************
 *  * Created by : arhan on 15 Dec, 2021 at 23:35.
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

sealed class CheckInAction {
    data class Update(val item: CheckInEntity): CheckInAction()
    data class Delete(val item: CheckInEntity): CheckInAction()
}

class MyCheckInDetailsBottomSheet (
    private val item: CheckInEntity,
    private val showAction: Boolean,
    private val onDialogDismiss : (action: CheckInAction?) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: LayoutMyCheckInActionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutMyCheckInActionBinding.inflate(inflater, container, false)
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

        setUiData()
        setClickAction()
    }

    private fun setUiData() {
        with(binding) {
            tvName.text = item.locationName
            tvDate.text = DateUtil.getTimeAgo(item.date)
            val formattedCount = MathUtil.prettyCount(item.count)
            val checkInCountStr = "$formattedCount check in"
            tvCheckInCount.text = checkInCountStr
            tvPrivacy.text = when(item.privacy) {
                Const.Params.CheckInPrivacy.ONLY_ME -> "Only Me"
                else -> "Public"
            }

            ivImage.load(item.locationImageUrl?: "") {
                placeholder(R.drawable.ic_location_placeholder)
                error(R.drawable.ic_location_placeholder)
            }

            groupAction.visibility = if(showAction) View.VISIBLE else View.GONE
        }
    }

    private fun setClickAction() {
        with(binding) {
            if(showAction) {
                btnChangePrivacy.setOnClickListener {
                    onDialogDismiss(CheckInAction.Update(item))
                    dismiss()
                }
                btnDelete.setOnClickListener {
                    onDialogDismiss(CheckInAction.Delete(item))
                    dismiss()
                }
            }
            btnDismiss.setOnClickListener { dismiss() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "MyCheckInActionBottomSheet"

        private fun newInstance(
            item: CheckInEntity,
            showAction: Boolean,
            onDismiss : (action: CheckInAction?) -> Unit
        ) = MyCheckInDetailsBottomSheet(item, showAction, onDismiss)

        fun show(
            fragmentManager: FragmentManager,
            item: CheckInEntity,
            showAction: Boolean,
            onDismiss : (action: CheckInAction?) -> Unit
        ): MyCheckInDetailsBottomSheet {
            val dialog = newInstance(item, showAction, onDismiss)
            // dialog.isCancelable = false
            dialog.show(fragmentManager, TAG)
            return dialog
        }
    }
}