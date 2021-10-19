package com.workfort.pstuian.app.ui.signin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.faculty.listener.BatchClickEvent
import com.workfort.pstuian.app.ui.faculty.listener.FacultyClickEvent
import com.workfort.pstuian.app.ui.forgotpassword.ForgotPasswordActivity
import com.workfort.pstuian.app.ui.signin.viewstate.SignInState
import com.workfort.pstuian.app.ui.signup.SignUpActivity
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.databinding.ActivitySignInBinding
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.helper.Toaster
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SignInActivity : BaseActivity<ActivitySignInBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivitySignInBinding
            = ActivitySignInBinding::inflate

    private val mViewModel: AuthViewModel by viewModel()

    override fun getToolbarId(): Int = R.id.toolbar

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        with(binding) {
            setClickListener(btnSignIn, btnSignUp, btnForgetPassword)
        }

        observeSignIn()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v) {
            binding.btnSignIn -> {
                signIn()
            }
            binding.btnSignUp -> {
                selectFaculty()
            }
            binding.btnForgetPassword -> {
                launchActivity<ForgotPasswordActivity>()
            }
            else -> {
                Timber.e("Who clicked me!")
            }
        }
    }

    private fun signIn() {
        with(binding) {
            val email = etEmail.text.toString()
            if(TextUtils.isEmpty(email)) {
                tilEmail.error = "*Required"
                return
            }
            tilEmail.error = null

            val pass = etPassword.text.toString()
            if(TextUtils.isEmpty(pass)) {
                tilPassword.error = "*Required"
                return
            }
            if(pass.length < 4) {
                tilPassword.error = "*Too short"
                return
            }
            tilPassword.error = null

            mViewModel.signIn(email, pass)
        }
    }

    private fun observeSignIn() {
        lifecycleScope.launch {
            mViewModel.signInState.collect {
                when (it) {
                    is SignInState.Idle -> {
                    }
                    is SignInState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                        binding.btnSignIn.isEnabled = false
                        binding.btnSignUp.isEnabled = false
                        binding.btnForgetPassword.isEnabled = false
                    }
                    is SignInState.Success -> {
                        binding.loader.visibility = View.INVISIBLE
                        Toaster.show("Successfully signed in!")
                        finish()
                    }
                    is SignInState.Error -> {
                        binding.loader.visibility = View.INVISIBLE
                        binding.btnSignIn.isEnabled = true
                        binding.btnSignUp.isEnabled = true
                        binding.btnForgetPassword.isEnabled = true
                        val title = it.error?: "Failed to Sign in"
                        val msg = getString(R.string.error_msg_sign_in)
                        CommonDialog.error(this@SignInActivity, title, msg)
                    }
                }
            }
        }
    }

    private fun selectFaculty() {
        FacultySelectorBottomSheet(object : FacultyClickEvent {
            override fun onClickFaculty(faculty: FacultyEntity) {
                selectBatch(faculty)
            }
        }).show(supportFragmentManager, FacultySelectorBottomSheet.TAG)
    }

    private fun selectBatch(faculty: FacultyEntity) {
        BatchSelectorBottomSheet(faculty, object : BatchClickEvent {
            override fun onClickBatch(batch: BatchEntity) {
                val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                intent.putExtra(Const.Key.FACULTY, faculty)
                intent.putExtra(Const.Key.BATCH, batch)
                startActivity(intent)
            }
        }).show(supportFragmentManager, BatchSelectorBottomSheet.TAG)
    }
}
