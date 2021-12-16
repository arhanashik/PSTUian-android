package com.workfort.pstuian.app.ui.signin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.common.bottomsheet.BatchSelectorBottomSheet
import com.workfort.pstuian.app.ui.common.bottomsheet.FacultySelectorBottomSheet
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.faculty.listener.BatchClickEvent
import com.workfort.pstuian.app.ui.forgotpassword.ForgotPasswordActivity
import com.workfort.pstuian.app.ui.signin.viewstate.SignInState
import com.workfort.pstuian.app.ui.signup.StudentSignUpActivity
import com.workfort.pstuian.app.ui.signup.TeacherSignUpActivity
import com.workfort.pstuian.databinding.ActivitySignInBinding
import com.workfort.pstuian.databinding.PromptSelectUserTypeBinding
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

    private var selectedUserType : String = Const.Params.UserType.STUDENT

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        with(binding.content) {
            tbUserType.addOnButtonCheckedListener { _, checkedId, _ ->
                selectedUserType = if(checkedId == R.id.btn_student)
                    Const.Params.UserType.STUDENT else Const.Params.UserType.TEACHER
            }

            setClickListener(btnSignIn, btnSignUp, btnForgetPassword)
        }

        observeSignIn()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        with(binding.content) {
            when(v) {
                btnSignIn -> signIn()
                btnSignUp -> selectUserTypeAndSignUp()
                btnForgetPassword -> launchActivity<ForgotPasswordActivity>()
                else -> Timber.e("Who clicked me!")
            }
        }
    }

    private fun signIn() {
        with(binding.content) {
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

            mViewModel.signIn(email, pass, selectedUserType)
        }
    }

    private fun observeSignIn() {
        lifecycleScope.launch {
            mViewModel.signInState.collect {
                when (it) {
                    is SignInState.Idle -> Unit
                    is SignInState.Loading -> setActionUiState(true)
                    is SignInState.Success -> {
                        setActionUiState(false)
                        Toaster.show("Successfully signed in!")
                        finish()
                    }
                    is SignInState.Error -> {
                        setActionUiState(false)
                        var msg = it.error?: "Failed to Sign in. "
                        if(selectedUserType == Const.Params.UserType.STUDENT)
                            msg += getString(R.string.error_msg_sign_in)
                        CommonDialog.error(this@SignInActivity, message = msg)
                    }
                }
            }
        }
    }

    private fun setActionUiState(isLoading: Boolean) {
        val visibility = if(isLoading) View.VISIBLE else View.GONE
        with(binding.content) {
            loader.visibility = visibility

            btnSignIn.isEnabled = !isLoading
            btnSignUp.isEnabled = !isLoading
            btnForgetPassword.isEnabled = !isLoading
        }
    }

    private fun selectUserTypeAndSignUp() {
        val binding = PromptSelectUserTypeBinding.inflate(layoutInflater,
            null, false)
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(binding.root)
            .create()

        var userType = Const.Params.UserType.STUDENT
        binding.tbUserType.addOnButtonCheckedListener { _, checkedId, _ ->
            userType = if(checkedId == R.id.btn_student)
                Const.Params.UserType.STUDENT else Const.Params.UserType.TEACHER
        }
        binding.btnSelect.setOnClickListener {
            dialog.dismiss()
            selectFaculty(userType)
        }
        binding.btnDismiss.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun selectFaculty(userType: String) {
        FacultySelectorBottomSheet { faculty ->
            if(userType == Const.Params.UserType.STUDENT) {
                selectBatch(faculty)
            } else {
                gotoTeacherSignUp(faculty)
                finish()
            }
        }.show(supportFragmentManager, FacultySelectorBottomSheet.TAG)
    }

    private fun selectBatch(faculty: FacultyEntity) {
        BatchSelectorBottomSheet(faculty, object : BatchClickEvent {
            override fun onClickBatch(batch: BatchEntity) {
                gotoStudentSignUp(faculty, batch)
                finish()
            }
        }).show(supportFragmentManager, BatchSelectorBottomSheet.TAG)
    }

    private fun gotoStudentSignUp(faculty: FacultyEntity, batch: BatchEntity) {
        val intent = Intent(this, StudentSignUpActivity::class.java).apply {
            putExtra(Const.Key.FACULTY, faculty)
            putExtra(Const.Key.BATCH, batch)
        }
        startActivity(intent)
    }

    private fun gotoTeacherSignUp(faculty: FacultyEntity) {
        val intent = Intent(this, TeacherSignUpActivity::class.java).apply {
            putExtra(Const.Key.FACULTY, faculty)
        }
        startActivity(intent)
    }
}
