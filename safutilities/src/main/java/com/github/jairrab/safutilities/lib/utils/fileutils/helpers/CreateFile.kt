package com.github.jairrab.safutilities.lib.utils.fileutils.helpers

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.functions.mimeType
import com.github.jairrab.safutilities.lib.FileProviderUtil
import java.io.File

internal class CreateFile(
    private val fileProviderUtil: FileProviderUtil
) {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun execute(
        fragment: Fragment,
        file: File,
        defaultFileName: String,
        requestCode: Int,
        authority: String,
        pickerInitialUri: Uri? = null
    ) {
        val intent = Intent().apply {
            val uri = fileProviderUtil.getFileProviderUri(authority, file)
            addCategory(Intent.CATEGORY_OPENABLE)
            action = Intent.ACTION_CREATE_DOCUMENT
            type = file.extension.mimeType()
            putExtra(Intent.EXTRA_TITLE, defaultFileName)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
        }
        fragment.startActivityForResult(intent, requestCode)
    }
}