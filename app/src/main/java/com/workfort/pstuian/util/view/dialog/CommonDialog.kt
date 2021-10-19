package com.workfort.pstuian.util.view.dialog

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.workfort.pstuian.R
import com.workfort.pstuian.databinding.PromptErrorBinding
import com.workfort.pstuian.databinding.PromptProPicChangeBinding
import com.workfort.pstuian.databinding.PromptSuccessBinding

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
        onBtnClick: () -> Unit = {},
        dismissOnBtnClick: Boolean = true,
        callback: SuccessDialogCallback? = null
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptSuccessBinding.inflate(inflater, null, false)
        binding.tvTitle.text = title
        binding.tvWarning.text = warning
        binding.tvMessage.text = message
        binding.btnDismiss.text = btnText
        if(TextUtils.isEmpty(warning)) binding.tvWarning.visibility = View.GONE

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .show()

        binding.btnDismiss.setOnClickListener {
            if(dismissOnBtnClick) dialog.dismiss()
            callback?.onClickDismiss()
            onBtnClick()
        }
        dialog.show()

        return dialog
    }

    interface SuccessDialogCallback {
        fun onClickDismiss()
    }

    fun error(
        context: Context,
        title: String = context.getString(R.string.default_error_dialog_title),
        message: String = context.getString(R.string.default_error_dialog_message),
        btnText: String = context.getString(R.string.txt_dismiss),
        cancelable: Boolean = true,
        onBtnClick: () -> Unit = {},
        dismissOnBtnClick: Boolean = true,
        callback: ErrorDialogCallback? = null,
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = PromptErrorBinding.inflate(inflater, null, false)
        binding.tvTitle.text = title
        binding.tvMessage.text = message
        binding.btnDismiss.text = btnText

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .show()

        binding.btnDismiss.setOnClickListener {
            if(dismissOnBtnClick) dialog.dismiss()
            callback?.onClickDismiss()
            onBtnClick()
        }
        dialog.show()

        return dialog
    }

    interface ErrorDialogCallback {
        fun onClickDismiss()
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
            .show()

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
}