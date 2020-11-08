package com.github.jairrab.safutilities.lib.utils.fileutils.helpers

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.SafUtilities.Companion.LOG_TAG
import com.github.jairrab.safutilities.functions.mimeType
import java.io.File

internal class OpenSavePicker {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun execute(
        fragment: Fragment,
        requestCode: Int,
        file: File,
        defaultFileName: String?,
        pickerInitialUri: Uri?
    ) {
        val intent = Intent().apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            action = Intent.ACTION_CREATE_DOCUMENT
            type = file.extension.mimeType()

            val title = defaultFileName ?: file.name
            putExtra(Intent.EXTRA_TITLE, title)

            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }

            val s1 = "requestCode:$requestCode"
            val s2 = "file:$file"
            val s3 = "defaultFileName:$defaultFileName"
            val s4 = "pickerInitialUri:$pickerInitialUri"
            Log.v(LOG_TAG, "Opening save picker- $s1 | $s2 | $s3 | $s4")
        }

        fragment.startActivityForResult(intent, requestCode)
    }
}