package com.example.githubsearch.core.utils

import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned

fun String.parseHtml(): Spanned {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).trim().toSpanned()
}