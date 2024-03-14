package com.kaushal.japacounter.ext

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.kaushal.japacounter.ui.widgets.MaterialAlertBuilder
import com.kaushal.japacounter.utils.AndroidMaterialAlertBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


inline fun View.hideKeyboard() {
    if (this.requestFocus()) {
        context.getSystemService<InputMethodManager>()
            ?.hideSoftInputFromWindow(windowToken, 0)
    }
}

inline val ViewGroup.layoutInflator: LayoutInflater
    get() = LayoutInflater.from(context)

inline val Context.localBroadcastManager: LocalBroadcastManager
    get() = LocalBroadcastManager.getInstance(this)

inline fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

inline fun convertInstantToString(json: Instant, moshi: Moshi): String =
    moshi.adapter<Instant>(Types.newParameterizedType(Instant::class.java, Instant::class.java)).toJson(json)

inline fun convertStringToInstant(objectData: String, moshi: Moshi): Instant? =
    moshi.adapter<Instant>(Types.newParameterizedType(Instant::class.java, Instant::class.java)).fromJson(objectData)

inline fun Instant.ofPattern(outPattern: String): String? = try {
    DateTimeFormatter.ofPattern(outPattern).withZone(ZoneId.systemDefault()).format(this)
} catch (ex: Exception) {
    ex.printStackTrace()
    null
}

fun Context.showMaterialAlert(init : MaterialAlertBuilder<DialogInterface>.() -> Unit): Dialog =
    AndroidMaterialAlertBuilder(this).apply { init() }.show()
