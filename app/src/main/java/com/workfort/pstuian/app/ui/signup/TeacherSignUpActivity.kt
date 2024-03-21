package com.workfort.pstuian.app.ui.signup

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.dialog.CommonDialog
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.signin.SignInActivity
import com.workfort.pstuian.app.ui.webview.WebViewActivity
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.databinding.ActivityTeacherSignUpBinding
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.util.extension.launchActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TeacherSignUpActivity : BaseActivity<ActivityTeacherSignUpBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTeacherSignUpBinding
            = ActivityTeacherSignUpBinding::inflate

    private val mViewModel: AuthViewModel by viewModel()
    private lateinit var mFaculty: FacultyEntity

    override fun getToolbarId(): Int = R.id.toolbar

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        val faculty = intent.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)
        if(faculty == null) finish()
        else mFaculty = faculty

        with(binding) {
            tvFaculty.text = mFaculty.title
            setClickListener(btnSignUp, btnSignIn, btnTermsAndConditions, btnPrivacyPolicy)
        }

        observeSignUp()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v) {
            binding.btnSignUp -> signUp()
            binding.btnSignIn -> gotToSignIn()
            binding.btnTermsAndConditions -> launchActivity<WebViewActivity>(Pair(
                Const.Key.URL,
                NetworkConst.Remote.TERMS_AND_CONDITIONS))
            binding.btnPrivacyPolicy -> launchActivity<WebViewActivity>(Pair(
                Const.Key.URL,
                NetworkConst.Remote.PRIVACY_POLICY))
            else -> Timber.e("Who clicked me!")
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

            val designation = etDesignation.text.toString()
            if(TextUtils.isEmpty(designation)) {
                tilDesignation.error = "*Required"
                return
            }
            tilDesignation.error = null

            val department = etDepartment.text.toString()
            if(TextUtils.isEmpty(department)) {
                tilDepartment.error = "*Required"
                return
            }
            tilDepartment.error = null

            val email = etEmail.text.toString()
            if(TextUtils.isEmpty(email)) {
                tilEmail.error = "*Required"
                return
            }
            tilEmail.error = null

            val password = etPassword.text.toString()
            if(TextUtils.isEmpty(password) || password.length < 6) {
                tilPassword.error = "*Required"
                return
            }
            tilPassword.error = null



            mViewModel.signUpTeacher(name, designation, department, email, password, mFaculty.id)
        }
    }

    private fun observeSignUp() {
        lifecycleScope.launch {
            mViewModel.teacherSignUpState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUiState(true)
                    is RequestState.Success<*> -> {
                        setActionUiState(false)
                        doSuccessAction()
                    }
                    is RequestState.Error -> {
                        setActionUiState(false)
                        val title = "Sign up failed!"
                        val msg = it.error?: getString(R.string.default_error_dialog_message)
                        CommonDialog.error(this@TeacherSignUpActivity, title, msg)
                    }
                }
            }
        }
    }

    private fun setActionUiState(isLoading: Boolean) {
        val visibility = if(isLoading) View.VISIBLE else View.GONE
        with(binding) {
            loader.visibility = visibility

            btnSignIn.isEnabled = !isLoading
            btnSignUp.isEnabled = !isLoading
            btnTermsAndConditions.isEnabled = !isLoading
            btnPrivacyPolicy.isEnabled = !isLoading
        }
    }

    private fun doSuccessAction() {
        val msg = getString(R.string.success_msg_sign_up)
        val btnTxt = getString(R.string.txt_sign_in)
        val warning = getString(R.string.warning_msg_sign_up)
        CommonDialog.success(this@TeacherSignUpActivity, message = msg, btnText = btnTxt,
            warning = warning, cancelable = false) {
            gotToSignIn()
        }
    }

    private fun gotToSignIn() {
        launchActivity<SignInActivity>()
        finish()
    }
}
