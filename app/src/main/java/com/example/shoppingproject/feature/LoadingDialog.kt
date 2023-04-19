package com.example.shoppingproject.feature

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.example.shoppingproject.R


class LoadingDialog(
    private val activity: Activity,
) {
    private var alertDialog: AlertDialog? = null

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading, null))
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.show()
    }


    fun dismissDialog() {
        alertDialog?.dismiss()
    }
}