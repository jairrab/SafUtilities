package com.github.jairrab.safutilities.lib

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

internal class FileProviderUtil(private val context: Context) {
    fun getFileProviderUri(authority: String, file: File): Uri {
        return FileProvider.getUriForFile(context, authority, file)
    }
}