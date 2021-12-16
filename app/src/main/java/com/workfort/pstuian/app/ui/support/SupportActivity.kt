package com.workfort.pstuian.app.ui.support

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.support.viewmodel.SupportViewModel
import com.workfort.pstuian.app.ui.support.viewstate.InquiryState
import com.workfort.pstuian.databinding.ActivitySupportBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SupportActivity : BaseActivity<ActivitySupportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivitySupportBinding
        = ActivitySupportBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private val mViewModel: SupportViewModel by viewModel()

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        setUiData()
        observeInquiry()
        binding.content.fabSend.setOnClickListener { sendQuery() }
    }

    private fun setUiData() {

    }

    private fun setActionUi(isLoading: Boolean) {
        val visibility = if(isLoading) View.VISIBLE else View.GONE
        with(binding) {
            loaderOverlay.visibility = visibility
            loader.visibility = visibility
            labelSaving.visibility = visibility
        }
    }

    private fun sendQuery() {
        with(binding.content) {
            val name = etName.text.toString()
            if(TextUtils.isEmpty(name)) {
                tilName.error = "*Required"
                return
            }
            tilName.error = null
            val email = etEmail.text.toString()
            if(TextUtils.isEmpty(email)) {
                tilEmail.error = "*Required"
                return
            }
            tilEmail.error = null
            val query = etQuery.text.toString()
            if(TextUtils.isEmpty(query)) {
                tilQuery.error = "*Required"
                return
            }
            tilQuery.error = null

            mViewModel.sendInquiry(name, email, query)
        }
    }

    private fun observeInquiry() {
        lifecycleScope.launch {
            mViewModel.inquiryState.collect {
                when (it) {
                    is InquiryState.Idle -> Unit
                    is InquiryState.Loading -> setActionUi(isLoading = true)
                    is InquiryState.Success -> {
                        setActionUi(isLoading = false)
                        CommonDialog.success(
                            this@SupportActivity,
                            message = it.message,
                            btnText = getString(R.string.txt_home),
                            cancelable = false,
                        ) { finish() }
                    }
                    is InquiryState.Error -> {
                        setActionUi(isLoading = false)
                        val error = it.error?: "Could not complete the request! Please try again."
                        CommonDialog.error(this@SupportActivity, message = error)
                    }
                }
            }
        }
    }
}
