package com.github.jairrab.safutilities.functions

import android.net.Uri
import java.net.URI
import java.net.URLConnection

fun URI.toAndroidUri(): Uri {
    return Uri.parse(toString())
}

fun Uri.toJavaUri(): URI {
    return URI(toString())
}

fun String.mimeType(): String {
    return when (this) {
        "jpg", "png" -> "image/*"
        "csv"        -> "text/csv"
        "html"       -> "text/html"
        "pdf"        -> "application/pdf"
        else         -> URLConnection.guessContentTypeFromName("filename.$this")
    }
}