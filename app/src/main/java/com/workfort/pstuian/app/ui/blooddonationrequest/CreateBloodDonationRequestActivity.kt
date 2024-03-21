package com.workfort.pstuian.app.ui.blooddonationrequest

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.blooddonationrequest.intent.BloodDonationIntent
import com.workfort.pstuian.app.ui.blooddonationrequest.viewmodel.BloodDonationViewModel
import com.workfort.pstuian.app.ui.common.dialog.CommonDialog
import com.workfort.pstuian.databinding.ActivityCreateBloodDonationRequestBinding
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.util.helper.DateUtil
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateBloodDonationRequestActivity : BaseActivity<ActivityCreateBloodDonationRequestBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityCreateBloodDonationRequestBinding
        = ActivityCreateBloodDonationRequestBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private val mViewModel: BloodDonationViewModel by viewModel()

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        setUiData()
        observeCreateRequest()
        binding.content.fabCreateRequest.setOnClickListener { createRequest() }
    }

    private fun setUiData() {
        with(binding.content) {
            // set blood group dropdown
            val groups = resources.getStringArray(R.array.blood_group)
            val bloodGroupAdapter = ArrayAdapter(this@CreateBloodDonationRequestActivity,
                R.layout.row_dropdown_item, groups)
            etBlood.setAdapter(bloodGroupAdapter)

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
            .setValidator(DateValidatorPointForward.now())
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
            val blood = etBlood.text.toString()
            if(TextUtils.isEmpty(blood)) {
                tilBlood.error = "*Required"
                return
            }
            tilBlood.error = null
            val dateBefore = etDate.text.toString()
            if(TextUtils.isEmpty(dateBefore)) {
                tilDate.error = "*Required"
                return
            }
            tilDate.error = null
            val contact = etContact.text.toString()
            if(TextUtils.isEmpty(contact)) {
                tilContact.error = "*Required"
                return
            }
            tilContact.error = null
            val info = etInfo.text.toString()

            lifecycleScope.launch {
                mViewModel.intent.send(BloodDonationIntent.CreateDonationRequest(
                    blood, dateBefore, contact, info
                ))
            }
        }
    }

    private fun observeCreateRequest() {
        lifecycleScope.launch {
            mViewModel.createDonationRequestState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUi(isLoading = true)
                    is RequestState.Success<*> -> {
                        setActionUi(isLoading = false)
                        val message = "Blood donation request created successfully!"
                        CommonDialog.success(
                            this@CreateBloodDonationRequestActivity,
                            message = message,
                            cancelable = false,
                        ) { finish() }
                    }
                    is RequestState.Error -> {
                        setActionUi(isLoading = false)
                        CommonDialog.error(this@CreateBloodDonationRequestActivity,
                            message = it.error.orEmpty())
                    }
                }
            }
        }
    }
}
