package com.mib.lib.mvvm.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mib.lib.mvvm.R
import com.mib.lib_navigation.DialogListener

class DialogUtils {
    companion object {
        fun showDialogWithTwoButtons(
            context: Context?,
            title: String,
            subtitle: String,
            left: String,
            right: String,
            dialogListener: DialogListener
        ) {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layoutView: View = inflater.inflate(R.layout.dialog_two_buttons, null)
            val tvTitle = layoutView.findViewById<TextView>(R.id.tvTitle)
            val tvSubTitle = layoutView.findViewById<TextView>(R.id.tvSubTitle)
            val btLeft = layoutView.findViewById<Button>(R.id.btLeft)
            val btRight = layoutView.findViewById<Button>(R.id.btRight)

            tvTitle.text = title
            tvSubTitle.text = subtitle
            btLeft.text = left
            btRight.text = right
            dialogBuilder.setView(layoutView)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            btLeft.setOnClickListener {
                alertDialog.dismiss()
                dialogListener.onLeftButtonClicked()
            }
            btRight.setOnClickListener {
                alertDialog.dismiss()
                dialogListener.onRightButtonClicked()
            }
        }
    }
}