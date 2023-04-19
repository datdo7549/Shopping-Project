package com.example.shoppingproject.feature

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.example.shoppingproject.R


class PaySuccessDialog(
    private val activity: Activity,
) {
    private var alertDialog: AlertDialog? = null

    fun show() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.pay_success_custom_dialog_loading, null))
        alertDialog = builder.create()
        alertDialog?.show()
    }


    fun dismissDialog() {
        alertDialog?.dismiss()
    }
}