package com.workfort.pstuian.app.ui.emailverification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.intent.AuthIntent
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.emailverification.viewstate.EmailVerificationState
import com.workfort.pstuian.databinding.ActivityEmailVerificationBinding
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : arhan on 16 Dec, 2021 at 10:55.
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

class EmailVerificationActivity : BaseActivity<ActivityEmailVerificationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityEmailVerificationBinding
            = ActivityEmailVerificationBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    private var selectedUserType : String = Const.Params.UserType.STUDENT

    private val mAuthViewModel by viewModel<AuthViewModel>()
    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        observeEmailVerification()
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
                btnSend -> sendEmailVerification()
                btnSignIn -> finish()
                else -> Timber.e("Who clicked me!")
            }
        }
    }

    private fun sendEmailVerification() {
        with(binding.content) {
            val email = etEmail.text.toString()
            if(email.isEmpty()) {
                tilEmail.error = "*required"
                return
            }
            tilEmail.error = null

            lifecycleScope.launch {
                mAuthViewModel.intent.send(AuthIntent.EmailVerification(selectedUserType, email))
            }
        }
    }

    private fun observeEmailVerification() {
        lifecycleScope.launch {
            mAuthViewModel.emailVerificationState.collect {
                when (it) {
                    is EmailVerificationState.Idle -> Unit
                    is EmailVerificationState.Loading -> setActionUiState(true)
                    is EmailVerificationState.Success -> {
                        setActionUiState(false)
                        CommonDialog.success(
                            this@EmailVerificationActivity,
                            message = it.message,
                            cancelable = false,
                        ) { finish() }
                    }
                    is EmailVerificationState.Error -> {
                        setActionUiState(false)
                        CommonDialog.error(
                            this@EmailVerificationActivity,
                            message = it.message
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