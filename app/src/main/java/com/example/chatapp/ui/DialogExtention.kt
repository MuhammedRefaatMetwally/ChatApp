package com.example.chatapp.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


fun Fragment.showMessage(
    message: String,
    posActionName: String? = null,
    posAction: DialogInterface.OnClickListener? = null,
    negActionName: String? = null,
    negAction: DialogInterface.OnClickListener? = null
): AlertDialog {
    val dialogBuilder = AlertDialog.Builder(requireContext())
    dialogBuilder.setMessage(message)

    if (posActionName != null) {
        dialogBuilder.setPositiveButton(posActionName, posAction)
    }

    if (negActionName != null) {
        dialogBuilder.setNegativeButton(negActionName, negAction)
    }

    return dialogBuilder.show()
}


fun Activity.showMessage(
    message: String,
    posActionName: String? = null,
    posAction: OnDialogActionClick? = null,
    negActionName: String? = null,
    negAction: OnDialogActionClick? = null,
    isCancelable: Boolean = true
): AlertDialog {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setMessage(message)

    if (posActionName != null) {
        dialogBuilder.setPositiveButton(posActionName) { dialog, i ->
            dialog.dismiss()
            posAction?.onActionClick()
        }
    }

    if (negActionName != null) {
        dialogBuilder.setNegativeButton(negActionName) { dialog, i ->
            dialog.dismiss()
            negAction?.onActionClick()
        }
    }
    dialogBuilder.setCancelable(isCancelable)

    return dialogBuilder.show()
}


fun Activity.showLoadingProgressDialog(
    message: String,                           //Not Recommended to use bad UX
    isCancelable: Boolean = true
): ProgressDialog {
    val alertDialog = ProgressDialog(this)
    alertDialog.setMessage(message)
    alertDialog.setCancelable(isCancelable)
    return alertDialog
}