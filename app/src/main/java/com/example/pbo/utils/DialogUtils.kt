package com.example.pbo.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.pbo.R
import com.google.android.material.button.MaterialButton

object DialogUtils { // Pakai 'object', JANGAN 'class'

    fun showUniversalDialog(
        context: Context,
        message: String,
        isConfirmation: Boolean = true,
        onConfirm: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        // ... (isi kode sama seperti jawaban sebelumnya) ...
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_notif)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.gravity = Gravity.CENTER
        dialog.setCancelable(true)

        val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
        val layoutButtons = dialog.findViewById<LinearLayout>(R.id.layoutButtons)
        val btnYes = dialog.findViewById<MaterialButton>(R.id.btnYes)
        val btnNo = dialog.findViewById<MaterialButton>(R.id.btnNo)

        tvMessage.text = message

        if (isConfirmation) {
            layoutButtons.visibility = View.VISIBLE
            btnYes.setOnClickListener {
                dialog.dismiss()
                onConfirm?.invoke()
            }
            btnNo.setOnClickListener {
                dialog.dismiss()
            }
        } else {
            layoutButtons.visibility = View.GONE
            dialog.setOnDismissListener {
                onDismiss?.invoke()
            }
        }
        dialog.show()
    }
}