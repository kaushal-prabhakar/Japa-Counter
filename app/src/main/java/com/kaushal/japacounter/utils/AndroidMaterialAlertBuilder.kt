package com.kaushal.japacounter.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kaushal.japacounter.ui.widgets.MaterialAlertBuilder

class AndroidMaterialAlertBuilder(override val context: Context) : MaterialAlertBuilder<AlertDialog> {

    private val builder = MaterialAlertDialogBuilder(context)

    override var isCancelable: Boolean
        get() = TODO("Not yet implemented")
        set(value) {
            builder.setCancelable(value)
        }
    override var messageResource: Int
        get() = TODO("Not yet implemented")
        set(value) {
            builder.setMessage(value)
        }
    override var titleResource: Int
        get() = TODO("Not yet implemented")
        set(value) {
            builder.setTitle(value)
        }

    override fun onCancelled(handler: (dialog: DialogInterface) -> Unit) {
        builder.setOnCancelListener(handler)
    }

    override fun positiveButton(buttonTextRes: Int, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setPositiveButton(buttonTextRes) { dialog, j ->
            onClicked(dialog)
        }
    }

    override fun negativeButton(buttonTextRes: Int, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNegativeButton(buttonTextRes) { dialog, j ->
            onClicked(dialog)
        }
    }

    override fun show(): AlertDialog = builder.show()

}