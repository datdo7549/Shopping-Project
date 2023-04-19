package com.example.shoppingproject.feature

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.shoppingproject.R
import com.example.shoppingproject.model.Product


class ExportSuccessDialog(
    private val activity: Activity,
) {
    private var alertDialog: AlertDialog? = null

    fun show(products: MutableList<Product>, doneClicked : () -> Any) {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        var bill = ""
        var mTotal = 0f
        products.forEach {
            val total = it.amount * it.productPrice
            bill += "+ ${it.productName}: ${it.amount} x ${it.productPrice} = ${total}\n"
            mTotal += total
        }
        bill += "Total: $mTotal"
        val view = inflater.inflate(R.layout.export_success_custom_dialog_loading, null)
        builder.setView(view)
        alertDialog = builder.create()
        val tvBill = view.findViewById<AppCompatTextView>(R.id.tvBill)
        tvBill?.text = bill

        val btnDone = view.findViewById<AppCompatButton>(R.id.btnDone)
        btnDone.setOnClickListener {
            doneClicked.invoke()
        }
        alertDialog?.show()
    }


    fun dismissDialog() {
        alertDialog?.dismiss()
    }
}