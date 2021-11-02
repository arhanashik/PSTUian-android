package com.workfort.pstuian.app.ui.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.forgotpassword.viewstate.ForgotPasswordState
import com.workfort.pstuian.databinding.ActivityForgotPasswordBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityForgotPasswordBinding
            = ActivityForgotPasswordBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private var selectedUserType : String = Const.Params.UserType.STUDENT

    private val mAuthViewModel by viewModel<AuthViewModel>()
    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        observeForgotPassword()
        with(binding.content) {
            tbUserType.addOnButtonCheckedListener { _, checkedId, _ ->
                selectedUserType = if(checkedId == R.id.btn_student)
                    Const.Params.UserType.STUDENT else Const.Params.UserType.TEACHER
            }
            setClickListener(btnSend, btnSignIn)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        with(binding.content) {
            when(v) {
                btnSend -> sendForgotPasswordEmail()
                btnSignIn -> finish()
                else -> Timber.e("Who clicked me!")
            }
        }
    }

    private fun sendForgotPasswordEmail() {
        with(binding.content) {
            val email = etEmail.text.toString()
            if(email.isEmpty()) {
                tilEmail.error = "*required"
                return
            }
            tilEmail.error = null

            mAuthViewModel.forgotPassword(selectedUserType, email)
        }
    }

    private fun observeForgotPassword() {
        lifecycleScope.launch {
            mAuthViewModel.forgotPasswordState.collect {
                when (it) {
                    is ForgotPasswordState.Idle -> Unit
                    is ForgotPasswordState.Loading -> {
                        setActionUiState(true)
                    }
                    is ForgotPasswordState.Success -> {
                        setActionUiState(false)
                        CommonDialog.success(
                            this@ForgotPasswordActivity,
                            message = it.message,
                            cancelable = false,
                            onBtnClick = { finish() }
                        )
                    }
                    is ForgotPasswordState.Error -> {
                        setActionUiState(false)
                        val message = it.error?: "Couldn't send reset email."
                        CommonDialog.error(
                            this@ForgotPasswordActivity,
                            message = message
                        )
                    }
                }
            }
        }
    }

    private fun setActionUiState(isLoading: Boolean) {
        val visibility = if(isLoading) View.VISIBLE else View.GONE
        with(binding.content) {
            loader.visibility = visibility

            btnSend.isEnabled = !isLoading
            btnSignIn.isEnabled = !isLoading
        }
    }
}
