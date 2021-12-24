package com.workfort.pstuian.app.ui.blooddonation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.blooddonation.BloodDonationEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.blooddonationrequest.intent.BloodDonationIntent
import com.workfort.pstuian.app.ui.blooddonationrequest.viewmodel.BloodDonationViewModel
import com.workfort.pstuian.app.ui.blooddonationrequest.viewstate.BloodDonationState
import com.workfort.pstuian.databinding.ActivityCreateBloodDonationBinding
import com.workfort.pstuian.util.helper.DateUtil
import com.workfort.pstuian.util.helper.Toaster
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  ****************************************************************************
 *  * Created by : arhan on 16 Dec, 2021 at 6:01.
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

class CreateBloodDonationActivity : BaseActivity<ActivityCreateBloodDonationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityCreateBloodDonationBinding
            = ActivityCreateBloodDonationBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private val mViewModel: BloodDonationViewModel by viewModel()
    private var mDonationItem: BloodDonationEntity? = null

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        mDonationItem = intent.getParcelableExtra(Const.Key.EXTRA_ITEM)

        setHomeEnabled()
        setUiData()
        observeCreateDonation()
        observeUpdateDonation()
        binding.content.fabCreate.setOnClickListener { createRequest() }
    }

    private fun setUiData() {
        with(binding.content) {
            // if an item is already available, we will update it instead of creating new
            mDonationItem?.let { item ->
                title = getString(R.string.txt_update)
                item.requestId?.toString().let { idStr ->
                    etRequestId.setText(idStr)
                }
                etDate.setText(item.date)
                etInfo.setText(item.info)
                fabCreate.setText(R.string.txt_update)
            }

            // set date picker
            etDate.apply {
                inputType = InputType.TYPE_NULL
                keyListener = null
                setOnClickListener { selectDate() }
                setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { selectDate() } }
            }
        }
    }

    private fun setActionUi(isLoading: Boolean) {
        val visibility = if(isLoading) View.VISIBLE else View.GONE
        with(binding) {
            loaderOverlay.visibility = visibility
            loader.visibility = visibility
            labelSaving.visibility = visibility
        }
    }

    private fun selectDate() {
        // Makes only dates from today forward selectable.
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTitleText("Select date")
            .setCalendarConstraints(constraintsBuilder)
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    val dateStr = DateUtil.format(it, "yyyy-MM-dd")
                    binding.content.etDate.setText(dateStr)
                }
                show(supportFragmentManager, "date-picker")
            }
    }

    private fun createRequest() {
        with(binding.content) {
            val requestIdStr = etRequestId.text.toString()
            val requestId = requestIdStr.toIntOrNull()?: 0
            val date = etDate.text.toString()
            if(TextUtils.isEmpty(date)) {
                tilDate.error = "*Required"
                return
            }
            tilDate.error = null
            val info = etInfo.text.toString()
            if(info.length > 500) {
                tilInfo.error = "*Maximum length 500"
                return
            }
            tilInfo.error = null

            lifecycleScope.launch {
                if(mDonationItem == null) {
                    mViewModel.intent.send(BloodDonationIntent
                        .CreateDonation(requestId, date, info))
                } else {
                    mDonationItem?.let {
                        it.requestId = requestId
                        it.date = date
                        it.info = info
                        mViewModel.intent.send(BloodDonationIntent.UpdateDonation(it))
                    }
                }
            }
        }
    }

    private fun observeCreateDonation() {
        lifecycleScope.launch {
            mViewModel.createDonationState.collect {
                when (it) {
                    is BloodDonationState.Idle -> Unit
                    is BloodDonationState.Loading -> setActionUi(isLoading = true)
                    is BloodDonationState.Success -> {
                        setActionUi(isLoading = false)
                        Toaster.show("Blood donation created successfully!")
                        setActivityResult(it.item, false)
                        finish()
                    }
                    is BloodDonationState.Error -> {
                        setActionUi(isLoading = false)
                        CommonDialog.error(this@CreateBloodDonationActivity,
                            message = it.message)
                    }
                }
            }
        }
    }

    private fun observeUpdateDonation() {
        lifecycleScope.launch {
            mViewModel.updateDonationState.collect {
                when (it) {
                    is BloodDonationState.Idle -> Unit
                    is BloodDonationState.Loading -> setActionUi(isLoading = true)
                    is BloodDonationState.Success -> {
                        setActionUi(isLoading = false)
                        Toaster.show("Blood donation updated successfully!")
                        setActivityResult(it.item, true)
                        finish()
                    }
                    is BloodDonationState.Error -> {
                        setActionUi(isLoading = false)
                        CommonDialog.error(this@CreateBloodDonationActivity,
                            message = it.message)
                    }
                }
            }
        }
    }

    private fun setActivityResult(item: BloodDonationEntity, isUpdated: Boolean) {
        val intent = Intent()
        intent.putExtra(Const.Key.EXTRA_ITEM, item)
        intent.putExtra(Const.Key.EXTRA_IS_UPDATED, isUpdated)
        setResult(Activity.RESULT_OK, intent)
    }
}