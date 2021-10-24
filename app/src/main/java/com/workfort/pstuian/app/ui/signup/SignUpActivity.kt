package com.workfort.pstuian.app.ui.signup

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
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.signup.viewstate.SignUpState
import com.workfort.pstuian.app.ui.webview.WebViewActivity
import com.workfort.pstuian.databinding.ActivitySignUpBinding
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.view.dialog.CommonDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivitySignUpBinding
            = ActivitySignUpBinding::inflate

    private val mViewModel: AuthViewModel by viewModel()
    private lateinit var mFaculty: FacultyEntity
    private lateinit var mBatch: BatchEntity

    override fun getToolbarId(): Int = R.id.toolbar

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        val faculty = intent.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)
        if(faculty == null) finish()
        else mFaculty = faculty

        val batch = intent.getParcelableExtra<BatchEntity>(Const.Key.BATCH)
        if(batch == null) finish()
        else mBatch = batch

        with(binding) {
            tvBatch.text = mBatch.name
            setClickListener(btnSignUp, btnSignIn, btnTermsAndConditions, btnPrivacyPolicy)
        }

        observeSignUp()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v) {
            binding.btnSignUp -> signUp()
            binding.btnSignIn -> finish()
            binding.btnTermsAndConditions -> launchActivity<WebViewActivity>(Pair(Const.Key.URL,
                Const.Remote.TERMS_AND_CONDITIONS))
            binding.btnPrivacyPolicy -> launchActivity<WebViewActivity>(Pair(Const.Key.URL,
                Const.Remote.PRIVACY_POLICY))
            else -> {
                Timber.e("Who clicked me!")
            }
        }
    }

    private fun signUp() {
        with(binding) {
            val name = etName.text.toString()
            if(TextUtils.isEmpty(name)) {
                tilName.error = "*Required"
                return
            }
            tilName.error = null

            val id = etId.text.toString()
            if(TextUtils.isEmpty(id)) {
                tilId.error = "*Required"
                return
            }
            tilId.error = null

            val reg = etReg.text.toString()
            if(TextUtils.isEmpty(reg)) {
                tilReg.error = "*Required"
                return
            }
            tilReg.error = null

            val session = etSession.text.toString()
            if(TextUtils.isEmpty(session)) {
                tilSession.error = "*Required"
                return
            }
            tilSession.error = null

            mViewModel.signUp(name, id, reg, mFaculty.id, mBatch.id, session)
        }
    }

    private fun observeSignUp() {
        lifecycleScope.launch {
            mViewModel.signUpState.collect {
                when (it) {
                    is SignUpState.Idle -> {
                    }
                    is SignUpState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                        binding.btnSignUp.isEnabled = false
                    }
                    is SignUpState.Success -> {
                        binding.loader.visibility = View.INVISIBLE
                        doSuccessAction()
                    }
                    is SignUpState.Error -> {
                        binding.loader.visibility = View.INVISIBLE
                        binding.btnSignUp.isEnabled = true
                        val title = "Sign up failed!"
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@SignUpActivity, title, msg)
                    }
                }
            }
        }
    }

    private fun doSuccessAction() {
        val msg = getString(R.string.success_msg_sign_up)
        val warning = getString(R.string.warning_msg_sign_up)
        CommonDialog.success(this@SignUpActivity, message = msg,
            warning = warning, cancelable = false,
            callback = object : CommonDialog.SuccessDialogCallback {
                override fun onClickDismiss() {
                    finish()
                }
        })
    }
}
