package com.kaushal.japacounter.ui.widgets

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes

interface MaterialAlertBuilder<out D : DialogInterface> {

    val context: Context

    var isCancelable: Boolean
    var messageResource: Int
    var titleResource: Int

    fun onCancelled(handler: (dialog: DialogInterface) -> Unit)
    fun positiveButton(
        @StringRes buttonTextRes: Int,
        onClicked: (dialog: DialogInterface) -> Unit
    )

    fun negativeButton(
        @StringRes buttonTextRes: Int,
        onClicked: (dialog: DialogInterface) -> Unit
    )

    fun show() : D

}