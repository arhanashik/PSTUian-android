package com.workfort.pstuian.util.view.dialog

import android.content.Context
import android.net.Uri
import android.text.*
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.checkin.CheckInEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.databinding.*
import com.workfort.pstuian.util.helper.nameFilter

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 5:24 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

object CommonDialog {
    fun success(
        context: Context,
        title: String = context.getString(R.string.default_success_dialog_title),
        message: String = context.getString(R.string.default_success_dialog_message),
        btnText: String = context.getString(R.string.txt_dismiss),
        warning: String = "",
        cancelable: Boolean = true,
        dismissOnBtnClick: Boolean = true,
        @DrawableRes icon: Int = R.drawable.ic_check_circle_fill,
        onBtnClick: () -> Unit = {},
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptSuccessBinding.inflate(inflater, null, false)
        with(binding) {
            ivIcon.setImageResource(icon)
            tvTitle.text = title
            tvWarning.text = warning
            tvMessage.text = message
            btnDismiss.text = btnText
            if(TextUtils.isEmpty(warning)) tvWarning.visibility = View.GONE
        }

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .create()

        binding.btnDismiss.setOnClickListener {
            if(dismissOnBtnClick) dialog.dismiss()
            onBtnClick()
        }
        dialog.show()

        return dialog
    }

    fun error(
        context: Context,
        title: String = context.getString(R.string.default_error_dialog_title),
        message: String = context.getString(R.string.default_error_dialog_message),
        btnText: String = context.getString(R.string.txt_dismiss),
        cancelable: Boolean = true,
        dismissOnBtnClick: Boolean = true,
        onBtnClick: () -> Unit = {},
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptErrorBinding.inflate(inflater, null, false)
        binding.tvTitle.text = title
        binding.tvMessage.text = message
        binding.btnDismiss.text = btnText

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .create()

        binding.btnDismiss.setOnClickListener {
            if(dismissOnBtnClick) dialog.dismiss()
            onBtnClick()
        }
        dialog.show()

        return dialog
    }

    fun changeProPic(
        context: Context,
        imgUri: Uri,
        onClickChange: () -> Unit = {},
        onClickUpload: () -> Unit = {},
        cancelable: Boolean = false,
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptProPicChangeBinding.inflate(inflater, null, false)
        binding.imgAvatar.load(imgUri)

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .create()

        binding.btnDismiss.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnChange.setOnClickListener {
            onClickChange()
        }

        binding.btnUpload.setOnClickListener {
            onClickUpload()
        }
        dialog.show()

        return dialog
    }

    fun changeName(
        context: Context,
        oldName: String,
        onClickChange: (newName: String) -> Unit = {},
        cancelable: Boolean = false,
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptChangeNameBinding.inflate(inflater, null, false)
        binding.etName.setText(oldName)
        binding.etName.setSelection(oldName.length)
        binding.etName.filters = arrayOf(nameFilter)
        binding.btnChange.isEnabled = false

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                when {
                    p0.isNullOrEmpty() || p0.trim().isEmpty() -> {
                        binding.tilName.error = "Name cannot be empty"
                        binding.btnChange.isEnabled = false
                    }
                    p0.toString() == oldName -> {
                        binding.tilName.error = "Name cannot be same as old name"
                        binding.btnChange.isEnabled = false
                    }
                    else -> {
                        binding.tilName.error = null
                        binding.btnChange.isEnabled = true
                    }
                }
            }
        }
        binding.etName.addTextChangedListener(textWatcher)

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .create()

        binding.btnDismiss.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnChange.setOnClickListener {
            val newName = binding.etName.text.toString().trim()
            onClickChange(newName)
        }

        dialog.show()

        return dialog
    }

    fun changeBio(
        context: Context,
        oldBio: String,
        onClickChange: (newBio: String) -> Unit = {},
        cancelable: Boolean = false,
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptChangeBioBinding.inflate(inflater, null, false)
        binding.etBio.setText(oldBio)
        binding.etBio.setSelection(oldBio.length)
        binding.btnChange.isEnabled = false

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                when {
                    p0.isNullOrEmpty() || p0.trim().isEmpty() -> {
                        binding.tilBio.error = "Bio cannot be empty"
                        binding.btnChange.isEnabled = false
                    }
                    p0.trim().length > 150 -> {
                        binding.tilBio.error = "Bio cannot be more than 150 character"
                        binding.btnChange.isEnabled = false
                    }
                    p0.toString() == oldBio -> {
                        binding.tilBio.error = "Bio cannot be same as old bio"
                        binding.btnChange.isEnabled = false
                    }
                    else -> {
                        binding.tilBio.error = null
                        binding.btnChange.isEnabled = true
                    }
                }
            }
        }
        binding.etBio.addTextChangedListener(textWatcher)

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .create()

        binding.btnDismiss.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnChange.setOnClickListener {
            val newBio = binding.etBio.text.toString().trim()
            onClickChange(newBio)
        }

        dialog.show()

        return dialog
    }

    fun changeCV(
        context: Context,
        pdfUri: Uri,
        onClickChange: () -> Unit = {},
        onClickUpload: (pdfUri: Uri) -> Unit = {},
        cancelable: Boolean = false,
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptChangeCvBinding.inflate(inflater, null, false)

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .create()

        binding.btnDismiss.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnChange.setOnClickListener {
            onClickChange()
        }

        binding.btnUpload.setOnClickListener {
            onClickUpload(pdfUri)
        }
        dialog.show()

        return dialog
    }

    fun changePassword(
        context: Context,
        onClickChange: (oldPassword: String, newPassword: String) -> Unit,
        cancelable: Boolean = false,
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptChangePasswordBinding.inflate(inflater, null, false)

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .create()

        binding.btnDismiss.setOnClickListener { dialog.dismiss() }
        binding.btnChange.setOnClickListener {
            val oldPassword = binding.etOldPassword.text.toString()
            if(oldPassword.isEmpty()) {
                binding.tilOldPassword.error = "*Required"
                return@setOnClickListener
            }
            if(oldPassword.length < 6) {
                binding.tilOldPassword.error = "Minimum length is 6"
                return@setOnClickListener
            }
            binding.tilOldPassword.error = null

            val newPassword = binding.etNewPassword.text.toString()
            if(newPassword.isEmpty()) {
                binding.tilNewPassword.error = "*Required"
                return@setOnClickListener
            }
            if(newPassword.length < 6) {
                binding.tilNewPassword.error = "Minimum length is 6"
                return@setOnClickListener
            }
            if(oldPassword == newPassword) {
                binding.tilNewPassword.error = "Passwords cannot be same."
                return@setOnClickListener
            }
            binding.tilNewPassword.error = null
            onClickChange(oldPassword, newPassword)
            dialog.dismiss()
        }
        dialog.show()

        return dialog
    }

    fun changePrivacy(
        context: Context,
        item: CheckInEntity,
        onChange: (privacy: String) -> Unit
    ) : AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptChangeCheckInPrivacyBinding.inflate(inflater,
            null, false)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .create()

        val prevCheckedId = if(item.privacy == Const.Params.CheckInPrivacy.PUBLIC)
            R.id.btn_public else R.id.btn_only_me
        var newPrivacy = Const.Params.CheckInPrivacy.PUBLIC
        binding.btgPrivacy.addOnButtonCheckedListener { _, checkedId, _ ->
            newPrivacy = if(checkedId == R.id.btn_public) {
                binding.tvMessage.text = context.getString(R.string.hint_privacy_public)
                Const.Params.CheckInPrivacy.PUBLIC
            } else {
                binding.tvMessage.text = context.getString(R.string.hint_privacy_only_me)
                Const.Params.CheckInPrivacy.ONLY_ME
            }
        }
        binding.btgPrivacy.check(prevCheckedId)

        binding.btnChange.setOnClickListener {
            dialog.dismiss()
            onChange(newPrivacy)
        }
        binding.btnDismiss.setOnClickListener { dialog.dismiss() }

        dialog.show()

        return dialog
    }

    fun confirmation(
        context: Context,
        title: String = context.getString(R.string.txt_delete),
        message: String = context.getString(R.string.msg_delete_permanent),
        confirmBtnTxt: String = context.getString(R.string.txt_delete),
        onConfirm: () -> Unit
    ) : AlertDialog {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(confirmBtnTxt) { dialog, _ ->
                dialog.dismiss()
                onConfirm()
            }
            .setNegativeButton(R.string.label_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()

        return dialog
    }
}