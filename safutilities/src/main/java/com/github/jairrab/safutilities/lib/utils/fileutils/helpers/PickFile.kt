package com.github.jairrab.safutilities.lib.utils.fileutils.helpers

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.SafUtilities.Companion.LOG_TAG
import com.github.jairrab.safutilities.model.MimeType

internal class PickFile {
    fun execute(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        initialUri: Uri?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pickFile(fragment, requestCode, mimeType, initialUri)
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = mimeType.value
            val s = "requestCode:$requestCode | mimeType:$mimeType | pickerInitialUri:$initialUri"
            Log.v(LOG_TAG, "Pick file- $s")
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun pickFile(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        initialUri: Uri? = null
    ) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

            type = mimeType.value

            if (mimeType == MimeType.ALL) {
                //see https://stackoverflow.com/a/55416383
                val mimeTypes = arrayOf(
                    "application/*",  //"audio/*",
                    "font/*",  //"image/*",
                    "message/*",
                    "model/*",
                    "multipart/*",
                    "text/*"
                )
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                //putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            if (initialUri != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)
            }
        }

        val s = "requestCode:$requestCode | mimeType:$mimeType | pickerInitialUri:$initialUri"
        Log.v(LOG_TAG, "Pick file- $s")
        fragment.startActivityForResult(intent, requestCode)
    }
}