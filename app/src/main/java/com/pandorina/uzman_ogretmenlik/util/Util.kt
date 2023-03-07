package com.pandorina.uzman_ogretmenlik.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pandorina.uzman_ogretmenlik.R


fun Fragment.showConfirmationDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    onClickPositive: () -> Unit
){
    MaterialAlertDialogBuilder(this.requireContext()).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButtonText) { _, _ ->
            onClickPositive()
        }
        setNegativeButton("İptal") { _, _ -> }
    }.show()
}

fun getSuccessPercentage(correctCount: Float?, wrongCount: Float?): Int {
    if (correctCount == null || wrongCount == null) return 0
    return ((correctCount / (correctCount + wrongCount)) * 100).toInt()
}

fun Int.getEmojiAnimationRes(): Int {
    return when(this){
        in 70..100 -> R.raw.lottie_reaction_happy
        in 50..69 -> R.raw.lottie_reaction_normal
        else -> R.raw.lottie_reaction_bad
    }
}

fun Context.initShareIntent(title: String, uri: String){
    Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, uri)
        type = "text/plain"
        startActivity(Intent.createChooser(this, title))
    }
}

fun Context.initMailIntent() {
    Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:ahmetfarukcuha@gmail.com")
        putExtra(Intent.EXTRA_SUBJECT, "Uzman Öğretmenlik Uygulaması - Öneri,İstek")
        startActivity(this)
    }

}

fun Context.initViewIntent(uri: String){
    Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(uri)
        startActivity(this)
    }
}

inline fun <reified T> T.toJson(): String? {
    return Gson().toJson(this)
}

inline fun <reified T> String.toObject(): T {
    val typeToken = object : TypeToken<T>() {}.type
    return Gson().fromJson<T>(this, typeToken)
}










